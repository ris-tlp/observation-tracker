package com.observatory.observationscheduler.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CelestialEventImageDto {

    @JsonProperty("celestial_event_image_id")
    private Long celestialEventImageId;

    private String url;

    private String uuid;

    public CelestialEventImageDto(Long celestialEventImageId, String url, String uuid) {
        this.celestialEventImageId = celestialEventImageId;
        this.url = url;
        this.uuid = uuid;
    }

    public Long getCelestialEventImageId() {
        return celestialEventImageId;
    }

    public void setCelestialEventImageId(Long celestialEventImageId) {
        this.celestialEventImageId = celestialEventImageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Getters and setters
    // toString
    // equals
    // hashCode
}
