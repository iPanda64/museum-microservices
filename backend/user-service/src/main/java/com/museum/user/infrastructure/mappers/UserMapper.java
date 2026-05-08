package com.museum.user.infrastructure.mappers;

import com.museum.user.domain.aggregate.User;
import com.museum.user.domain.aggregate.UserId;
import com.museum.user.infrastructure.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return new User(
                new UserId(entity.getUserId()),
                entity.getEmail(),
                entity.getPhoneNumber()
        );
    }
    public UserEntity toEntity(User domain) {
        if (domain == null) return null;

        return new UserEntity(
                domain.userId() != null ? domain.userId().value() : null,
                domain.email(),
                domain.phoneNumber()
        );
    }
}
