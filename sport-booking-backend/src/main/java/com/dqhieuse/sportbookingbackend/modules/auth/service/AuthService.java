package com.dqhieuse.sportbookingbackend.modules.auth.service;

import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.RegisterRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.Role;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.Status;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.auth.mapper.UserMapper;
import com.dqhieuse.sportbookingbackend.modules.auth.repository.UserRepository;
import com.dqhieuse.sportbookingbackend.modules.auth.utils.JwtUtils;
import com.dqhieuse.sportbookingbackend.modules.wallet.service.WalletService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public User register(RegisterRequest registerRequest) {
        // Check duplicate account
        if (userRepository.existsUserByEmail(registerRequest.email())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (userRepository.existsUserByUsername(registerRequest.username())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        // Map DTO -> Entity
        User newUser = userMapper.toEntity(registerRequest);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setStatus(Status.ACTIVE);

        // Save user into database
        User savedUser = userRepository.save(newUser);

        // Create wallet for new user
        walletService.createWallet(savedUser);

        return savedUser;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtils.generateToken(userDetails);

            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            String userRole = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            return LoginResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .username(userDetails.getUsername())
                    .role(userRole)
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    public LoginResponse refreshToken(String refreshToken) {
        try {
            final String username = jwtUtils.extractUsername(refreshToken);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (!jwtUtils.isTokenValid(refreshToken, userDetails)) {
                throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
            }

            String newToken = jwtUtils.generateToken(userDetails);

            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

            return LoginResponse.builder()
                    .role(userDetails.getAuthorities().iterator().next().getAuthority())
                    .username(userDetails.getUsername())
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } catch (ExpiredJwtException e) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Refresh token expired. Please login again");
        } catch (UsernameNotFoundException e) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "User not found for the given refresh token");
        } catch (Exception e) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Error occurred while refreshing token.");
        }
    }

    public Optional<String> extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst();
    }
}
