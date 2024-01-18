package com.observatory.observationscheduler.observation;

import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Observation")
public class Observation {
    @Id
    @GeneratedValue
    private Long id;

    private String observationName;

    @CreationTimestamp
    private Date createdTimestamp;

    @UpdateTimestamp
    private Date updatedTimestamp;

    @ManyToOne
    private UserAccount owner;

    public Observation() {
    }

//    public Observation(String observationName, Date createdTimestamp, Date updatedTimestamp, UserAccount owner) {
//        this.observationName = observationName;
//        this.createdTimestamp = createdTimestamp;
//        this.updatedTimestamp = updatedTimestamp;
//        this.owner = owner;
//    }


    public Observation(String observationName, UserAccount owner) {
        this.observationName = observationName;
        this.owner = owner;
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

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "id=" + id +
                ", observationName='" + observationName + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                ", owner=" + owner +
                '}';
    }
}
