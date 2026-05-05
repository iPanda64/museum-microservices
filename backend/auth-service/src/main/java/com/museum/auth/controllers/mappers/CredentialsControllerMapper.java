package com.museum.auth.controllers.mappers;

import com.museum.auth.domain.dtos.CredentialsRequestDto;
import com.museum.auth.domain.dtos.CredentialsResponseDto;
import com.museum.auth.domain.aggregate.*;
import com.museum.auth.services.InvalidRequestException; // NEW IMPORT
import org.springframework.stereotype.Component;

@Component
public class CredentialsControllerMapper {

    public Credentials toDomain(CredentialsRequestDto dto) {
        if (dto == null) return null;

        Integer roleId = null;
        if (dto.roleName() != null) {
            try {
                roleId = RoleName.valueOf(dto.roleName().toUpperCase()).getId();
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("Invalid role name: " + dto.roleName()); // MODIFIED LINE
            }
        }

        return new Credentials(
                dto.userId() != null ? new UserId(dto.userId()) : null,
                dto.username() != null ? new Username(dto.username()) : null,
                dto.password(), // Mapping raw 'password' from DTO to 'passwordHash' in Domain
                roleId
        );
    }

    public NullCredentials toNullDomain(CredentialsRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Integer roleId = null;
        if (dto.roleName() != null && !dto.roleName().isBlank()) {
            try {
                roleId = RoleName.valueOf(dto.roleName().toUpperCase()).getId();
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("Invalid role name: " + dto.roleName());
            }
        }

        return new NullCredentials(
                dto.userId() != null ? new UserId(dto.userId()) : null,
                dto.username() != null ? new Username(dto.username()) : null,
                dto.password(),
                roleId
        );
    }

    public CredentialsResponseDto toResponseDto(Credentials domain) {
        if (domain == null) return null;

        return new CredentialsResponseDto(
                domain.userId() != null ? domain.userId().value() : null,
                domain.username() != null ? domain.username().value() : null,
                domain.roleId() != null ? RoleName.fromId(domain.roleId()).name() : null
        );
    }
}
