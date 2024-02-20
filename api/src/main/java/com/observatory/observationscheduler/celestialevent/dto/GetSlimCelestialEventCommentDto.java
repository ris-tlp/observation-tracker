package com.observatory.observationscheduler.celestialevent.dto;

import com.observatory.observationscheduler.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;

public class GetSlimCelestialEventCommentDto {
    private String content;
    private GetUserAccountDto author;
    private String uuid;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GetUserAccountDto getAuthor() {
        return author;
    }

    public void setAuthor(GetUserAccountDto author) {
        this.author = author;
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
