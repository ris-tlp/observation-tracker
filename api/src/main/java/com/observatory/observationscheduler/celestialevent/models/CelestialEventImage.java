package com.observatory.observationscheduler.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
public class CelestialEventImage {
    @Id
    @GeneratedValue
    @Column(name = "celestial_event_image_id")
    private long celestialEventImageId;

    @ManyToOne
//    @JsonBackReference
    @JoinColumn(name = "celestial_event_id", nullable = false)
    private CelestialEvent celestialEvent;

    private String url;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private String uuid;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}