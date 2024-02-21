package com.observatory.observationscheduler.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class CelestialEventComment {
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

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CelestialEventComment> replies;

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

//    public void addReply(CelestialEventComment reply) {
//        if (replies == null) {
//            replies = new ArrayList<>();
//        }
//        replies.add(reply);
//    }

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
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
