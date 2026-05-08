package com.museum.user.domain.aggregate;

/**
 * A data holder that allows nulls, used for processing partial updates for User.
 */
public record NullUser(
        String email,
        String phoneNumber
) {}
