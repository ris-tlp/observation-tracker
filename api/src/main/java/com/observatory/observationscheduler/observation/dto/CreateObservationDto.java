package com.observatory.observationscheduler.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class CreateObservationDto {
    private String observationName;

    private String observationDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

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

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    @Override
    public String toString() {
        return "CreateObservationDto{" +
                "observationName='" + observationName + '\'' +
                ", observationDescription='" + observationDescription + '\'' +
                ", observationDateTime=" + observationDateTime +
                '}';
    }
}
