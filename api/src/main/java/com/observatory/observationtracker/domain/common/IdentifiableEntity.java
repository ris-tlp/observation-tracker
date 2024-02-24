package com.observatory.observationtracker.domain.common;

import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
public class IdentifiableEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    protected String uuid;

    @CreationTimestamp
    protected Timestamp createdTimestamp;

    @UpdateTimestamp
    protected Timestamp updatedTimestamp;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
    }


    @Override
    public String toString() {
        return "IdentifiableEntity{" +
                "uuid='" + uuid + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}
