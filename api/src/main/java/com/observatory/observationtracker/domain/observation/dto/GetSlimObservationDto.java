package com.observatory.observationtracker.domain.observation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.observatory.observationtracker.domain.celestialevent.dto.GetSlimCelestialEventDto;
import com.observatory.observationtracker.domain.common.IdentifiableDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class GetSlimObservationDto extends IdentifiableDto {
    private String observationName;

    private String observationDescription;

    private List<GetObservationImageDto> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime observationDateTime;

    private GetUserAccountDto owner;

    private GetSlimCelestialEventDto celestialEvent;

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
