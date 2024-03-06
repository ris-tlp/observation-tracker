package com.observatory.observationtracker.domain.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationtracker.domain.celestialevent.dto.GetSlimCelestialEventDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


// @TODO: Fix author showing as null
public class GetSlimObservationDto {
    private String uuid;

    private String observationName;

    private String observationDescription;

    private List<GetObservationImageDto> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

    private Timestamp createdTimestamp;

    private Timestamp updatedTimestamp;

    private GetUserAccountDto owner;

    private GetSlimCelestialEventDto celestialEvent;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public List<GetObservationImageDto> getImages() {
        return images;
    }

    public void setImages(List<GetObservationImageDto> images) {
        this.images = images;
    }

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
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

    public GetUserAccountDto getOwner() {
        return owner;
    }

    public void setOwner(GetUserAccountDto owner) {
        this.owner = owner;
    }

    public GetSlimCelestialEventDto getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(GetSlimCelestialEventDto celestialEvent) {
        this.celestialEvent = celestialEvent;
    }
}
