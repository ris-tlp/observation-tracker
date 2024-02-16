package com.observatory.observationscheduler.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GetCelestialEventDto {
    private String celestialEventName;

    private String celestialEventDescription;

    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime celestialEventDateTime;

    private List<GetCelestialEventImageDto> images;


    private CelestialEventStatus eventStatus;

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

    public List<GetCelestialEventImageDto> getImages() {
        return images;
    }

    public void setImages(List<GetCelestialEventImageDto> images) {
        this.images = images;
    }

    public CelestialEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(CelestialEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
