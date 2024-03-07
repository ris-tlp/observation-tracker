package com.observatory.observationtracker.domain.observation.dto;

import com.observatory.observationtracker.domain.common.IdentifiableDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;
import java.util.List;

public class GetObservationCommentDto extends IdentifiableDto {
    private String content;

    private GetUserAccountDto author;

    private List<GetObservationCommentDto> replies;

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

    public List<GetObservationCommentDto> getReplies() {
        return replies;
    }

    public void setReplies(List<GetObservationCommentDto> replies) {
        this.replies = replies;
    }
}
