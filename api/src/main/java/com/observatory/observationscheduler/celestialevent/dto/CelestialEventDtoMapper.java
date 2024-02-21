package com.observatory.observationscheduler.celestialevent.dto;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CelestialEventDtoMapper {
    GetCelestialEventDto celestialEventToGetDto(CelestialEvent celestialEvent);

    List<GetCelestialEventDto> celestialEventListToGetDtoList(List<CelestialEvent> celestialEvents);

    GetCelestialEventImageDto celestialEventImageToGetDto(CelestialEventImage celestialEventImage);

    List<GetCelestialEventImageDto> celestialEventImageListToGetDtoList(List<CelestialEventImage> celestialEventImages);

    CelestialEvent createDtoToCelestialEvent(CreateCelestialEventDto createCelestialEventDto);

    CelestialEventComment createCommentDtoToCelestialEventComment(CreateCelestialEventCommentDto createCelestialEventCommentDto);

    GetSlimCelestialEventCommentDto celestialEventReplyToGetSlimCelestialEventCommentDto(CelestialEventComment comment);

    GetCelestialEventCommentDto celestialEventCommentToGetCelestialEventCommentDto(CelestialEventComment comment);

    List<GetCelestialEventCommentDto> celestialEventCommentsToGetCelestialEventCommentDtos(List<CelestialEventComment> comments);


}
