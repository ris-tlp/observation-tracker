package com.observatory.observationtracker.domain.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetCelestialEventDto extends GetSlimCelestialEventDto{
    private String celestialEventName;

    private String celestialEventDescription;

    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime celestialEventDateTime;

    private List<GetCelestialEventCommentDto> comments;

    private List<GetCelestialEventImageDto> images;

    private Timestamp createdTimestamp;

    private Timestamp updatedTimestamp;

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

    public List<GetCelestialEventCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<GetCelestialEventCommentDto> comments) {
        this.comments = comments;
    }
}
