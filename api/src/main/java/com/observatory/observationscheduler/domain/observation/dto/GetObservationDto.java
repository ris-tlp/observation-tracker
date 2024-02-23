package com.observatory.observationscheduler.domain.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.observatory.observationscheduler.domain.celestialevent.dto.GetSlimCelestialEventDto;
import com.observatory.observationscheduler.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetObservationDto {
    private String observationName;

    private String observationDescription;

    private Boolean isPublished;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

    private String uuid;

    private Timestamp createdTimestamp;

    private Timestamp updatedTimestamp;

    private GetSlimCelestialEventDto celestialEvent;

    private GetUserAccountDto owner;

    private List<GetObservationImageDto> images;

    private List<GetObservationCommentDto> comments;

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

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
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

    public GetSlimCelestialEventDto getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(GetSlimCelestialEventDto celestialEvent) {
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

    public List<GetObservationCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<GetObservationCommentDto> comments) {
        this.comments = comments;
    }
}