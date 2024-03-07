package com.observatory.observationtracker.domain.celestialevent.dto;

import com.observatory.observationtracker.domain.common.IdentifiableDto;

import java.sql.Timestamp;

public class GetCelestialEventImageDto extends IdentifiableDto {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
