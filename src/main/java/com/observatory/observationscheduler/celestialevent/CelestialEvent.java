package com.observatory.observationscheduler.celestialevent;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    private LocalDateTime celestialEventDateTime;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @Enumerated(EnumType.STRING)
    private CelestialEventStatus eventStatus;


    public CelestialEvent() {
    }

    public CelestialEvent(String celestialEventName, String celestialEventDescription, LocalDateTime celestialEventTime) {
        this.celestialEventName = celestialEventName;
        this.celestialEventDescription = celestialEventDescription;
        this.celestialEventDateTime = celestialEventTime;
    }

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
        this.setEventStatus(CelestialEventStatus.UPCOMING);
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    /*
     * Inefficient, used to change status of an event according to the current date.
     */
    @PostLoad
    private void updateEventStatus() {
        if (this.getCelestialEventDateTime().isBefore(LocalDateTime.now())) {
            System.out.println("IN HERE");
            this.setEventStatus(CelestialEventStatus.COMPLETED);
        }

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

    public LocalDateTime getCelestialEventDateTime() {
        return celestialEventDateTime;
    }

    public void setCelestialEventDateTime(LocalDateTime celestialEventTime) {
        this.celestialEventDateTime = celestialEventTime;
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

    public CelestialEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(CelestialEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
