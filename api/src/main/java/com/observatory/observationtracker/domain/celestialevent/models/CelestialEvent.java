package com.observatory.observationtracker.domain.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class CelestialEvent extends IdentifiableEntity {
    @Id
    @GeneratedValue
    @Column(name = "celestial_event_id")
    private Long celestialEventId;

    @Column(nullable = false)
    private String celestialEventName;

    private String celestialEventDescription;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "celestialEvent",
            cascade = CascadeType.ALL
    )
    private List<CelestialEventImage> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime celestialEventDateTime;

    @OneToMany(
            mappedBy = "celestialEvent",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<CelestialEventComment> comments;

    @Enumerated(EnumType.STRING)
    private CelestialEventStatus eventStatus;


    public CelestialEvent() {
    }

    public CelestialEvent(String celestialEventName, String celestialEventDescription,
                          LocalDateTime celestialEventTime) {
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


    public List<CelestialEventImage> convertImageToCelestialEventImage(List<String> imageUrls) {
        return imageUrls.stream().filter(Objects::nonNull).map(url -> new CelestialEventImage(this, url)).toList();
    }

    public Long getCelestialEventId() {
        return celestialEventId;
    }

    public void setCelestialEventId(Long id) {
        this.celestialEventId = id;
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

    public CelestialEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(CelestialEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<CelestialEventImage> getImages() {
        return images;
    }

    public void setImages(List<CelestialEventImage> images) {
        this.images = images;
    }

    public List<CelestialEventComment> getComments() {
        return comments;
    }

    public void setComments(List<CelestialEventComment> comments) {
        this.comments = comments;
    }

}
