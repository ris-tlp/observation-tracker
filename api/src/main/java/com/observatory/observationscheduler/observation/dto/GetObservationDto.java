package com.observatory.observationscheduler.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationscheduler.celestialevent.dto.GetCelestialEventDto;
import com.observatory.observationscheduler.useraccount.GetUserAccountDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetObservationDto {
    private String observationName;

    private String observationDescription;

    //@TODO change fix ispublished because showing null
    private Boolean isPublished;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

    private String uuid;

    private Timestamp createdTimestamp;

    private Timestamp updatedTimestamp;

    private GetCelestialEventDto celestialEvent;

    private GetUserAccountDto owner;

    private List<GetObservationImageDto> images;

    public String getObservationName() {
        return observationName;
    }

    public void setObservationName(String observationName) {
        this.observationName = observationName;
    }

    public String getObservationDescription() {
        return observationDescription;
    }

    public void setObservationDescription(String observationDescription) {
        this.observationDescription = observationDescription;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public GetCelestialEventDto getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(GetCelestialEventDto celestialEvent) {
        this.celestialEvent = celestialEvent;
    }

    public GetUserAccountDto getOwner() {
        return owner;
    }

    public void setOwner(GetUserAccountDto owner) {
        this.owner = owner;
    }

    public List<GetObservationImageDto> getImages() {
        return images;
    }

    public void setImages(List<GetObservationImageDto> images) {
        this.images = images;
    }
}
