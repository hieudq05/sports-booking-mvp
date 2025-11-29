package com.dqhieuse.sportbookingbackend.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    String password,

    @NotBlank(message = "Full name cannot be blank")
    String fullName,

    @Pattern(regexp = "^\\d{10,15}$", message = "Phone number must be digits only, 10-15 chars")
    String phoneNumber
) {}
