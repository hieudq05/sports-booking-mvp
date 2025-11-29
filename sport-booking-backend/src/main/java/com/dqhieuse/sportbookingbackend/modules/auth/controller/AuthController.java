package com.dqhieuse.sportbookingbackend.modules.auth.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.LoginResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.RegisterRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.UserResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.auth.mapper.UserMapper;
import com.dqhieuse.sportbookingbackend.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User registeredUser = authService.register(registerRequest);

        UserResponse registeredUserResponse = userMapper.toResponse(registeredUser);

        return ApiResponse.created(registeredUserResponse, "User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);

        return ApiResponse.success(loginResponse, "Login successfully");
    }
}
