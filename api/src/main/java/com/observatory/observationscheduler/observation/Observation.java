package com.observatory.observationscheduler.observation;

import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Observation {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String observationName;

    private String observationDescription;

    private String observationImage;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @ManyToOne
    private UserAccount owner;

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

    public Observation(String observationName, String observationDescription, String observationImage, UserAccount owner) {
        this.observationName = observationName;
        this.observationDescription = observationDescription;
        this.owner = owner;
        this.observationImage = observationImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getObservationImage() {
        return observationImage;
    }

    public void setObservationImage(String observationImage) {
        this.observationImage = observationImage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "id=" + id +
                ", observationName='" + observationName + '\'' +
                ", observationDescription='" + observationDescription + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", owner=" + owner +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
