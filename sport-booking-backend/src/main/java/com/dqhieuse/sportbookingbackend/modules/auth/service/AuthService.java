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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

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

            return LoginResponse.builder()
                    .token(token)
                    .username(userDetails.getUsername())
                    .role(userDetails.getAuthorities().iterator().next().getAuthority())
                    .build();

        } catch (AuthenticationException e) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}
