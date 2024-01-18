package com.observatory.observationscheduler.useraccount;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserAccount {
    @Id
    @GeneratedValue
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;


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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
