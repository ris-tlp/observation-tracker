package com.observatory.observationtracker.domain.useraccount.dto;

import com.observatory.observationtracker.domain.useraccount.UserAccount;

public class GetUserAccountDto {
    private String name;

    private String email;

    private String uuid;

    public GetUserAccountDto(UserAccount user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.uuid = user.getUuid();
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
