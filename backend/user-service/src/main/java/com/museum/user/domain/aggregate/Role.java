package com.museum.user.domain.aggregate;

public record Role(Integer roleId, RoleName roleName) {

    public Role {
        if (roleId == null || roleId <= 0)
            throw new IllegalArgumentException("Role ID must be a valid, positive number.");

        if (roleName == null)
            throw new IllegalArgumentException("Role name must not be null.");
    }
}