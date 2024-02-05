package com.observatory.observationscheduler.celestialevent;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
public class CelestialEvent {
    @Id
    @GeneratedValue
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String celestialEventName;

    private String celestialEventDescription;

    private Date celestialEventTime;

    @CreationTimestamp
    private Date createdTimestamp;

    @UpdateTimestamp
    private Date updatedTimestamp;

    public CelestialEvent() {
    }

    public CelestialEvent(String celestialEventName, String celestialEventDescription, Date celestialEventTime) {
        this.celestialEventName = celestialEventName;
        this.celestialEventDescription = celestialEventDescription;
        this.celestialEventTime = celestialEventTime;
    }

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public Date getCelestialEventTime() {
        return celestialEventTime;
    }

    public void setCelestialEventTime(Date celestialEventTime) {
        this.celestialEventTime = celestialEventTime;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
