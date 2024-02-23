package com.observatory.observationscheduler.observation.models;

import com.fasterxml.jackson.annotation.*;
import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
// @TODO: Link Observation to ObservationComment - done
// @TODO: Create ObservationCommentDto and SlimDto for parsing - have to create slim
// @TODO: Create controller endpoints to create reply and create comment - done
// @TODO Split assembler into two different ones
// @TODO: Create repo with custom query - 1/2 done
// @TODO: integrate comment replies properly to specific fetches and bulk fetches
@Entity
public class Observation {
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

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @ManyToOne
    @JoinColumn(name = "celestial_event_id", nullable = false)
    private CelestialEvent celestialEvent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount owner;

    @JsonManagedReference
    @OneToMany(mappedBy = "observation", cascade = CascadeType.ALL)
    private List<ObservationImage> images;


    @OneToMany(mappedBy = "observation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ObservationComment> comments;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private String uuid;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
//                ", celestialEvent=" + celestialEvent +
                ", owner=" + owner +
                ", images=" + images +
                ", comments=" + comments +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
