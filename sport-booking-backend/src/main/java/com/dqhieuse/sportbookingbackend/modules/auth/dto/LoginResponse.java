package com.dqhieuse.sportbookingbackend.modules.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token,
        String username,
        String role
) {
}
