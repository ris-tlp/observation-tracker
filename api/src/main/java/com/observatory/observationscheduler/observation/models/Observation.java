package com.observatory.observationscheduler.observation.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Observation {
    @Id
    @GeneratedValue
    @JsonIgnore
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
    private UserAccount owner;

    @JsonManagedReference
    @OneToMany(mappedBy = "observation", cascade = CascadeType.ALL)
    private List<ObservationImage> images;

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
                       LocalDateTime observationDateTime) {
        this.observationName = observationName;
        this.observationDescription = observationDescription;
        this.owner = owner;
        this.observationDateTime = observationDateTime;
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

    public List<ObservationImage> getImages() {
        return images;
    }

    public void setImages(List<ObservationImage> images) {
        this.images = images;
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

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "id=" + observationId +
                ", observationName='" + observationName + '\'' +
                ", observationDescription='" + observationDescription + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", owner=" + owner +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
