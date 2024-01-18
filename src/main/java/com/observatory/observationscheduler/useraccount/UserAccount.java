package com.observatory.observationscheduler.useraccount;

import jakarta.persistence.*;

@Entity
public class UserAccount {
    @Id @GeneratedValue
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;


    public UserAccount() {
    }

    public UserAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
