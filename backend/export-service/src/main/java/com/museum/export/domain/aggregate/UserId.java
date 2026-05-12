package com.museum.export.domain.aggregate;

public record UserId(Integer value){

    public UserId {
        if (value == null)
            throw new IllegalArgumentException("Export ID must not be null.");
    }
}
