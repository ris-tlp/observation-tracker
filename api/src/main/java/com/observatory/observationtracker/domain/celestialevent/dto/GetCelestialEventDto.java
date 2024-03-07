package com.observatory.observationtracker.domain.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetCelestialEventDto extends GetSlimCelestialEventDto{
    private String celestialEventName;

    private String celestialEventDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime celestialEventDateTime;

    private List<GetCelestialEventCommentDto> comments;

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

    public List<GetCelestialEventCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<GetCelestialEventCommentDto> comments) {
        this.comments = comments;
    }
}
