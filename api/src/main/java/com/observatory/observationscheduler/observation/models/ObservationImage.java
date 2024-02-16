package com.observatory.observationscheduler.observation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
public class ObservationImage {
    @Id
    @GeneratedValue
    @JsonIgnore
    @Column(name = "observation_image_id")
    private long observationImageId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "observation_id", nullable = false)
    private Observation observation;

    private String url;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    public ObservationImage() {
    }

    public ObservationImage(Observation observation, String url) {
        this.observation = observation;
        this.url = url;
    }

    public long getObservationImageId() {
        return observationImageId;
    }

    public void setObservationImageId(long id) {
        this.observationImageId = id;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
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
}
