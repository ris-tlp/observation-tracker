package com.observatory.observationscheduler.celestialevent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.observatory.observationscheduler.useraccount.UserAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.*;

@Entity
public class CelestialEventComment {
    @Id
    @GeneratedValue
    @Column(name = "celestial_event_comment_id")
    private long celestialEventCommentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private UserAccount author;

    @ManyToOne
    private CelestialEvent celestialEvent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CelestialEventComment parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<CelestialEventComment> children;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp updatedTimestamp;

    @PrePersist
    private void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    public void addReply(CelestialEventComment reply) {
        if (this.children == null)
            this.children = new HashSet<>();

        this.children.add(reply);
    }


    public long getCelestialEventCommentId() {
        return celestialEventCommentId;
    }

    public void setCelestialEventCommentId(long celestialEventCommentId) {
        this.celestialEventCommentId = celestialEventCommentId;
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

    public CelestialEvent getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(CelestialEvent celestialEvent) {
        this.celestialEvent = celestialEvent;
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

    public CelestialEventComment getParent() {
        return parent;
    }

    public void setParent(CelestialEventComment parent) {
        this.parent = parent;
    }

    public Set<CelestialEventComment> getChildren() {
        return children;
    }

    public void setChildren(Set<CelestialEventComment> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "CelestialEventComment{" +
                "celestialEventCommentId=" + celestialEventCommentId +
                ", content='" + content + '\'' +
                ", author=" + author +
                '}';
    }
}
