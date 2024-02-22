package com.observatory.observationscheduler.observation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
public class ObservationComment {
    @Id
    @GeneratedValue
    @Column(name = "observation_comment_id")
    private Long observationCommentId;

    private String content;

    @ManyToOne
    private UserAccount author;

    @JsonBackReference
    @ManyToOne
    private Observation observation;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private ObservationComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ObservationComment> replies;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private String uuid;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @PreUpdate
    private void updateTimestamp() {
        this.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public ObservationComment() {}

    public Long getObservationCommentId() {
        return observationCommentId;
    }

    public void setObservationCommentId(Long observationCommentId) {
        this.observationCommentId = observationCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public void setAuthor(UserAccount author) {
        this.author = author;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public ObservationComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ObservationComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<ObservationComment> getReplies() {
        return replies;
    }

    public void setReplies(List<ObservationComment> replies) {
        this.replies = replies;
    }

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
}
