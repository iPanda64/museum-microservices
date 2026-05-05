package com.museum.auth.domain.models;

import java.util.Arrays;

public enum RoleName {
    ADMIN(1),
    MANAGER(2),
    EMPLOYEE(3),
    USER(4);

    private final int id;

    RoleName(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RoleName fromId(int id) {
        return Arrays.stream(RoleName.values())
                .filter(role -> role.id == id)
                .findFirst()
                .orElse(USER);
    }
}
