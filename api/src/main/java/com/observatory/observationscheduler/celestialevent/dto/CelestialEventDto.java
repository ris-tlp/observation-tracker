package com.observatory.observationscheduler.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class CelestialEventDto {
    @JsonProperty("celestial_event_id")
    private Long celestialEventId;

    private String celestialEventName;

    private String celestialEventDescription;

    private String uuid;

    private LocalDateTime celestialEventDateTime;

    private List<CelestialEventImageDto> images;

    private CelestialEventStatus eventStatus;

    public CelestialEventDto(Long celestialEventId, String celestialEventName, String celestialEventDescription,
                             String uuid, LocalDateTime celestialEventDateTime, List<CelestialEventImageDto> images,
                             CelestialEventStatus eventStatus) {
        this.celestialEventId = celestialEventId;
        this.celestialEventName = celestialEventName;
        this.celestialEventDescription = celestialEventDescription;
        this.uuid = uuid;
        this.celestialEventDateTime = celestialEventDateTime;
        this.images = images;
        this.eventStatus = eventStatus;
    }

    public Long getCelestialEventId() {
        return celestialEventId;
    }

    public void setCelestialEventId(Long celestialEventId) {
        this.celestialEventId = celestialEventId;
    }

    public String getCelestialEventName() {
        return celestialEventName;
    }

    public void setCelestialEventName(String celestialEventName) {
        this.celestialEventName = celestialEventName;
    }

    public String getCelestialEventDescription() {
        return celestialEventDescription;
    }

    public void setCelestialEventDescription(String celestialEventDescription) {
        this.celestialEventDescription = celestialEventDescription;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCelestialEventDateTime() {
        return celestialEventDateTime;
    }

    public void setCelestialEventDateTime(LocalDateTime celestialEventDateTime) {
        this.celestialEventDateTime = celestialEventDateTime;
    }

    public List<CelestialEventImageDto> getImages() {
        return images;
    }

    public void setImages(List<CelestialEventImageDto> images) {
        this.images = images;
    }

    public CelestialEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(CelestialEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
