package com.observatory.observationtracker.domain.observation.models;

import com.fasterxml.jackson.annotation.*;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import jakarta.persistence.*;

//import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Observation extends IdentifiableEntity {
    @Id
    @GeneratedValue
    @Column(name = "observation_id")
    private Long observationId;

    @Column(nullable = false)
    private String observationName;

    private String observationDescription;

    @Column(nullable = false)
    private Boolean isPublished = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime observationDateTime;

    @ManyToOne
    @JoinColumn(name = "celestial_event_id", nullable = false)
    private CelestialEvent celestialEvent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount owner;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "observation",
            cascade = CascadeType.ALL
    )
    private List<ObservationImage> images;


    @OneToMany(
            mappedBy = "observation",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<ObservationComment> comments;


    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public Observation() {
    }

    public Observation(String observationName, String observationDescription, UserAccount owner,
                       LocalDateTime observationDateTime, CelestialEvent celestialEvent) {
        this.observationName = observationName;
        this.observationDescription = observationDescription;
        this.owner = owner;
        this.observationDateTime = observationDateTime;
        this.celestialEvent = celestialEvent;
    }

    public List<ObservationImage> convertImageToObservationImage(List<String> imageUrls) {
        return imageUrls.stream().filter(Objects::nonNull).map(url -> new ObservationImage(this, url)).toList();
    }

    public Long getObservationId() {
        return observationId;
    }

    public void setObservationId(Long id) {
        this.observationId = id;
    }

    public String getObservationName() {
        return observationName;
    }

    public void setObservationName(String observationName) {
        this.observationName = observationName;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
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

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean published) {
        isPublished = published;
    }

    public CelestialEvent getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(CelestialEvent celestialEvent) {
        this.celestialEvent = celestialEvent;
    }

    public List<ObservationImage> getImages() {
        return images;
    }

    public void setImages(List<ObservationImage> images) {
        this.images = images;
    }

    public List<ObservationComment> getComments() {
        return comments;
    }

    public void setComments(List<ObservationComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "observationId=" + observationId +
                ", observationName='" + observationName + '\'' +
                ", observationDescription='" + observationDescription + '\'' +
                ", isPublished=" + isPublished +
                ", observationDateTime=" + observationDateTime +
//                ", celestialEvent=" + celestialEvent +
                ", owner=" + owner +
                ", images=" + images +
                ", comments=" + comments +
                '}';
    }
}
