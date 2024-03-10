package com.observatory.observationtracker.domain.observation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
public class ObservationComment extends IdentifiableEntity {
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

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<ObservationComment> replies;

    public ObservationComment() {
    }

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

    @Override
    public String toString() {
        return "ObservationComment{" +
                "observationCommentId=" + observationCommentId +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", parentComment=" + parentComment +
                ", replies=" + replies +
                '}';
    }
}
