package com.observatory.observationtracker.rabbitmq.notifications;


import com.observatory.observationtracker.domain.observation.dto.GetObservationCommentDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;

public record ObservationCommentNotification(
        GetUserAccountDto observationAuthor,
        GetObservationCommentDto createdComment
) {}