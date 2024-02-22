package com.observatory.observationscheduler.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetSlimCelestialEventDto {
    private String uuid;
    private String celestialEventName;
    private String celestialEventDescription;
    private List<GetCelestialEventImageDto> images;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime celestialEventDateTime;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;
    private CelestialEventStatus eventStatus;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public List<GetCelestialEventImageDto> getImages() {
        return images;
    }

    public void setImages(List<GetCelestialEventImageDto> images) {
        this.images = images;
    }

    public LocalDateTime getCelestialEventDateTime() {
        return celestialEventDateTime;
    }

    public void setCelestialEventDateTime(LocalDateTime celestialEventDateTime) {
        this.celestialEventDateTime = celestialEventDateTime;
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

    public CelestialEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(CelestialEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    @Override
    public String toString() {
        return "GetSlimCelestialEventDto{" +
                "uuid='" + uuid + '\'' +
                ", celestialEventName='" + celestialEventName + '\'' +
                ", celestialEventDescription='" + celestialEventDescription + '\'' +
                ", images=" + images +
                ", celestialEventDateTime=" + celestialEventDateTime +
//                ", createdTimestamp=" + createdTimestamp +
//                ", updatedTimestamp=" + updatedTimestamp +
                ", eventStatus=" + eventStatus +
                '}';
    }
}
