package com.observatory.observationtracker.domain.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationtracker.domain.common.IdentifiableEntity;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
public class CelestialEventComment extends IdentifiableEntity {
    @Id
    @GeneratedValue
    @Column(name = "celestial_event_comment_id")
    private Long celestialEventCommentId;

    private String content;

    @ManyToOne
    private UserAccount author;

    @JsonBackReference
    @ManyToOne
    private CelestialEvent celestialEvent;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private CelestialEventComment parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<CelestialEventComment> replies;


    public CelestialEventComment() {
    }

    public CelestialEvent getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(CelestialEvent celestialEvent) {
        this.celestialEvent = celestialEvent;
    }

    public Long getCelestialEventCommentId() {
        return celestialEventCommentId;
    }

    public void setCelestialEventCommentId(Long celestialEventCommentId) {
        this.celestialEventCommentId = celestialEventCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CelestialEventComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(CelestialEventComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<CelestialEventComment> getReplies() {
        return replies;
    }

    public void setReplies(List<CelestialEventComment> replies) {
        this.replies = replies;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public void setAuthor(UserAccount author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CelestialEventComment{" +
                "celestialEventCommentId=" + celestialEventCommentId +
                ", content='" + content + '\'' +
                ", parentComment=" + parentComment +
                ", replies=" + replies +
                '}';
    }
}
