package com.museum.user.domain.aggregate;

public record User(
        UserId userId,
        String email,
        String phoneNumber
) {
    public User {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("email must not be null or blank");

        if (phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("phoneNumber must not be null or blank");
    }

    public User changeEmail(String newEmail) {
        return new User(this.userId, newEmail, this.phoneNumber);
    }

    public User changePhoneNumber(String newPhoneNumber) {
        return new User(this.userId, this.email, newPhoneNumber);
    }
}
