package com.observatory.observationscheduler.useraccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;
import java.sql.Timestamp;

@Entity
public class UserAccount {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    public UserAccount() {
    }

    public UserAccount(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        return "UserAccount{" + "userId=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", uuid=" + uuid + '}';
    }
}
