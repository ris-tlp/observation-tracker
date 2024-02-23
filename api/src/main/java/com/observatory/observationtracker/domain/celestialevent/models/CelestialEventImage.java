package com.observatory.observationtracker.domain.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import jakarta.persistence.*;

@Entity
public class CelestialEventImage extends IdentifiableEntity {
    @Id
    @GeneratedValue
    @Column(name = "celestial_event_image_id")
    private long celestialEventImageId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "celestial_event_id", nullable = false)
    private CelestialEvent celestialEvent;

    private String url;

    public CelestialEventImage(CelestialEvent celestialEvent, String url) {
        this.celestialEvent = celestialEvent;
        this.url = url;
    }

    public CelestialEventImage() {
    }

    public long getCelestialEventImageId() {
        return celestialEventImageId;
    }

    public void setCelestialEventImageId(long celestialEventImageId) {
        this.celestialEventImageId = celestialEventImageId;
    }

    public CelestialEvent getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(CelestialEvent celestialEvent) {
        this.celestialEvent = celestialEvent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "CelestialEventImage{" +
                "url='" + url + '\'' +
                '}';
    }
}