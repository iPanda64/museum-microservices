package com.museum.auth.domain.aggregate;

public record Credentials(
        UserId userId,
        Username username,
        String passwordHash,
        Integer roleId
) {
    public Credentials {

        if (username == null)
            throw new IllegalArgumentException("username must not be null");

        if (passwordHash == null)
            throw new IllegalArgumentException("passwordHash must not be null");

        passwordHash = passwordHash.trim();
        if (passwordHash.isEmpty())
            throw new IllegalArgumentException("passwordHash must not be blank");

        if (roleId == null)
            throw new IllegalArgumentException("roleId must not be null");
    }

    public Credentials changeUsername(Username newUsername) {
        return new Credentials(this.userId, newUsername, this.passwordHash, this.roleId);
    }

    public Credentials changePasswordHash(String newPasswordHash) {
        return new Credentials(this.userId, this.username, newPasswordHash, this.roleId);
    }
}
