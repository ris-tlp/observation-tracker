package com.observatory.observationtracker.domain.useraccount.dto;

import com.observatory.observationtracker.domain.common.IdentifiableDto;

public class GetUserAccountDto extends IdentifiableDto {
    private String name;

    private String email;


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
}
