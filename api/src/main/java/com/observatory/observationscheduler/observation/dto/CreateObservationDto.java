package com.observatory.observationscheduler.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

// @TODO make mapper and test
// @TODO see if you need constductors
public class CreateObservationDto {
//    @JsonProperty("observation_id")
//    private Long observationId;

    private String observationName;

    private String observationDescription;

//    private Boolean isPublished;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

//    private CelestialEventDto celestialEvent;

//    private UserAccountDto owner;
//    private List<ObservationImage> images;
//    private List<ObservationImageDto> images;
//    private String uuid;

//    public Long getObservationId() {
//        return observationId;
//    }
//
//    public void setObservationId(Long observationId) {
//        this.observationId = observationId;
//    }

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

//    public Boolean getPublished() {
//        return isPublished;
//    }
//
//    public void setPublished(Boolean published) {
//        isPublished = published;
//    }

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

//    public CelestialEventDto getCelestialEvent() {
//        return celestialEvent;
//    }
//
//    public void setCelestialEvent(CelestialEventDto celestialEvent) {
//        this.celestialEvent = celestialEvent;
//    }

//    public UserAccountDto getOwner() {
//        return owner;
//    }
//
//    public void setOwner(UserAccountDto owner) {
//        this.owner = owner;
//    }

//    public List<ObservationImageDto> getImages() {
//        return images;
//    }

//    public void setImages(List<ObservationImageDto> images) {
//        this.images = images;
//    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

    @Override
    public String toString() {
        return "CreateObservationDto{" +
                "observationName='" + observationName + '\'' +
                ", observationDescription='" + observationDescription + '\'' +
                ", observationDateTime=" + observationDateTime +
                '}';
    }


    // Getters and setters
    // toString
    // equals
    // hashCode
}
