package com.observatory.observationtracker.domain.celestialevent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CreateCelestialEventDto {

    private String celestialEventName;

    private String celestialEventDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime celestialEventDateTime;

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
}
