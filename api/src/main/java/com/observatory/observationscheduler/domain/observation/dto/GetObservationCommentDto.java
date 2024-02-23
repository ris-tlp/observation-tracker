package com.observatory.observationscheduler.domain.observation.dto;

import com.observatory.observationscheduler.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.util.List;

public class GetObservationCommentDto {
    private String content;
    private String uuid;
    private GetUserAccountDto author;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;
    private List<GetObservationCommentDto> replies;

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

    public List<GetObservationCommentDto> getReplies() {
        return replies;
    }

    public void setReplies(List<GetObservationCommentDto> replies) {
        this.replies = replies;
    }
}
