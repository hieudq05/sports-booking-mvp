package com.dqhieuse.sportbookingbackend.modules.auth.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.RegisterRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.UserResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.auth.mapper.UserMapper;
import com.dqhieuse.sportbookingbackend.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User registeredUser = authService.register(registerRequest);

        UserResponse registeredUserResponse = userMapper.toResponse(registeredUser);

        return ApiResponse.created(registeredUserResponse, "User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(refreshExpiration))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // Return without a refresh token in the response body
        LoginResponse responseBody = LoginResponse.builder()
                .token(loginResponse.token())
                .username(loginResponse.username())
                .role(loginResponse.role())
                .build();

        return ApiResponse.success(responseBody, "Login successfully");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        ResponseCookie emptyCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, emptyCookie.toString());

        return ApiResponse.success(null, "Logout successfully");
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authService.extractRefreshTokenFromCookie(request)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "Refresh token not found in cookies"));

        LoginResponse newLoginResponse = authService.refreshToken(refreshToken);

        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refresh_token", newLoginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(refreshExpiration))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

        LoginResponse responseBody = LoginResponse.builder()
                .token(newLoginResponse.token())
                .username(newLoginResponse.username())
                .role(newLoginResponse.role())
                .build();

        return ApiResponse.success(responseBody, "Refresh token successfully");
    }
}
