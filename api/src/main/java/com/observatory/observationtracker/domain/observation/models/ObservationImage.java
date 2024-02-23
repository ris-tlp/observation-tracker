package com.observatory.observationtracker.domain.observation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import jakarta.persistence.*;

@Entity
public class ObservationImage extends IdentifiableEntity {
    @Id
    @GeneratedValue
    @Column(name = "observation_image_id")
    private long observationImageId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "observation_id", nullable = false)
    private Observation observation;

    private String url;

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

    @Override
    public String toString() {
        return "ObservationImage{" +
                "observationImageId=" + observationImageId +
                ", url='" + url + '\'' +
                '}';
    }
}
