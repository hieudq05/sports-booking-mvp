package com.dqhieuse.sportbookingbackend.modules.auth.mapper;

import com.dqhieuse.sportbookingbackend.modules.auth.dto.RegisterRequest;
import com.dqhieuse.sportbookingbackend.modules.auth.dto.UserResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    UserResponse toResponse(User user);
}
