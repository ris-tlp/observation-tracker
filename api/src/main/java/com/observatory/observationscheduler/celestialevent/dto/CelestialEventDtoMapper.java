package com.observatory.observationscheduler.celestialevent.dto;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CelestialEventDtoMapper {
    GetCelestialEventDto celestialEventToGetDto(CelestialEvent celestialEvent);


    @Named("toSlimDto")
    GetSlimCelestialEventDto celestialEventToGetSlimDto(CelestialEvent celestialEvent);

    @IterableMapping(qualifiedByName = "toSlimDto")
    List<GetSlimCelestialEventDto> celestialEventListToGetSlimDtoList(List<CelestialEvent> celestialEvents);

    GetCelestialEventImageDto celestialEventImageToGetDto(CelestialEventImage celestialEventImage);

    List<GetCelestialEventImageDto> celestialEventImageListToGetDtoList(List<CelestialEventImage> celestialEventImages);

    CelestialEvent createDtoToCelestialEvent(CreateCelestialEventDto createCelestialEventDto);

    CelestialEventComment createDtoToCelestialEventComment(CreateCelestialEventCommentDto createCelestialEventCommentDto);

    GetSlimCelestialEventCommentDto celestialEventReplyToGetSlimDto(CelestialEventComment comment);

    GetCelestialEventCommentDto celestialEventCommentToGetDto(CelestialEventComment comment);

    List<GetCelestialEventCommentDto> celestialEventCommentListToGetListDto(List<CelestialEventComment> comments);


}
