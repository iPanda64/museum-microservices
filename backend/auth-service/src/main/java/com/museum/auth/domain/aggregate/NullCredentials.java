package com.museum.auth.domain.aggregate;

/**
 * A data holder that allows nulls, used for processing partial updates for Credentials.
 * The 'password' field holds a raw password, not a hash.
 */
public record NullCredentials(
        UserId userId,
        Username username,
        String password,
        Integer roleId
) {}
