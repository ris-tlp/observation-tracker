package com.observatory.observationtracker.domain.celestialevent.dto;

import com.observatory.observationtracker.domain.common.IdentifiableDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;

import java.sql.Timestamp;


// Primarily used to return a reply once created
public class GetSlimCelestialEventCommentDto extends IdentifiableDto {
    private String content;

    private GetUserAccountDto author;


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

}
