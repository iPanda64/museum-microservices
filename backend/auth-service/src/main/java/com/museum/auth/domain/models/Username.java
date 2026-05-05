package com.museum.auth.domain.models;

public record Username(String value) {

    public Username {
        if (value == null)
            throw new IllegalArgumentException("value must not be null");

        value = value.trim();
        if (value.isEmpty())
            throw new IllegalArgumentException("value must not be blank");
    }
}
