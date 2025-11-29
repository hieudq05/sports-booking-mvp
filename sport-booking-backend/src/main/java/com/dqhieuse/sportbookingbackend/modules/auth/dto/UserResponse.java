package com.dqhieuse.sportbookingbackend.modules.auth.dto;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;
}
