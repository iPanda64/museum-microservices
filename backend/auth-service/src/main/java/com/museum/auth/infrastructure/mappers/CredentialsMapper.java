package com.museum.auth.infrastructure.mappers;

import com.museum.auth.domain.aggregate.Credentials;
import com.museum.auth.domain.aggregate.UserId;
import com.museum.auth.domain.aggregate.Username;
import com.museum.auth.infrastructure.entities.CredentialsEntity;
import com.museum.auth.infrastructure.entities.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class CredentialsMapper {

    public Credentials toDomain(CredentialsEntity entity) {
        if (entity == null) return null;

        return new Credentials(
                new UserId(entity.getUserId()),
                new Username(entity.getUsername()),
                entity.getPasswordHash(),
                entity.getRole() != null ? entity.getRole().getId() : null
        );
    }
    public CredentialsEntity toEntity(Credentials domain) {
        if (domain == null) return null;

        return new CredentialsEntity(
                domain.userId() != null ? domain.userId().value() : null,
                domain.username() != null ? domain.username().value() : null,
                domain.passwordHash(),
                domain.roleId() != null ? new RoleEntity(domain.roleId(), null) : null
        );
    }
}
