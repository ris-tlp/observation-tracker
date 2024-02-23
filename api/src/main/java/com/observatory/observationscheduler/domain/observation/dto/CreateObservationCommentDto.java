package com.observatory.observationscheduler.domain.observation.dto;

public class CreateObservationCommentDto {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CreateObservationCommentDto{" +
                "content='" + content + '\'' +
                '}';
    }
}
