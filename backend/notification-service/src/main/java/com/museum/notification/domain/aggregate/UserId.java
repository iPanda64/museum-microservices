package com.museum.notification.domain.aggregate;

public record UserId(Integer value){

    public UserId {
        if (value == null)
            throw new IllegalArgumentException("User ID must not be null.");
    }
}
