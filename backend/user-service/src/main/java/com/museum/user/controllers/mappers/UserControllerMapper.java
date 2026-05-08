package com.museum.user.controllers.mappers;

import com.museum.user.domain.dtos.UserRequestDto;
import com.museum.user.domain.dtos.UserResponseDto;
import com.museum.user.domain.aggregate.*;
import org.springframework.stereotype.Component;

@Component
public class UserControllerMapper {

    public User toDomain(Integer userId, UserRequestDto dto) {
        if (dto == null) return null;

        return new User(
                userId != null ? new UserId(userId) : null,
                dto.email(),
                dto.phoneNumber()
        );
    }

    public NullUser toNullDomain(UserRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return new NullUser(
                dto.email(),
                dto.phoneNumber()
        );
    }

    public UserResponseDto toResponseDto(User domain) {
        if (domain == null) return null;

        return new UserResponseDto(
                domain.userId() != null ? domain.userId().value() : null,
                domain.email(),
                domain.phoneNumber()
        );
    }
}
