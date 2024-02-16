package com.observatory.observationscheduler.useraccount;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountDto {
    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private String email;

    private String uuid;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
