package com.observatory.observationtracker.domain.observation.dto;

import com.observatory.observationtracker.domain.common.IdentifiableDto;


public class GetObservationImageDto extends IdentifiableDto {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
