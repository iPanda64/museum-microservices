package com.museum.user.infrastructure.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    public UserEntity() {
    }

    public UserEntity(Integer userId, String email, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
