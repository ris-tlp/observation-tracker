package com.observatory.observationscheduler.celestialevent.dto;

import com.observatory.observationscheduler.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.util.List;

public class GetCelestialEventCommentDto {
    private String content;
    private GetUserAccountDto author;
    private String uuid;
    private GetCelestialEventDto celestialEvent;
    private GetSlimCelestialEventCommentDto parent;
    private List<GetSlimCelestialEventCommentDto> children;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GetUserAccountDto getAuthor() {
        return author;
    }

    public void setAuthor(GetUserAccountDto author) {
        this.author = author;
    }

    public GetCelestialEventDto getCelestialEvent() {
        return celestialEvent;
    }

    public void setCelestialEvent(GetCelestialEventDto celestialEvent) {
        this.celestialEvent = celestialEvent;
    }

    public GetSlimCelestialEventCommentDto getParent() {
        return parent;
    }

    public void setParent(GetSlimCelestialEventCommentDto parent) {
        this.parent = parent;
    }

    public List<GetSlimCelestialEventCommentDto> getChildren() {
        return children;
    }

    public void setChildren(List<GetSlimCelestialEventCommentDto> children) {
        this.children = children;
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
