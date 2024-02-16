package com.observatory.observationscheduler.celestialevent.dto;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CelestialEventDtoMapper {
    GetCelestialEventDto celestialEventToGetDto(CelestialEvent celestialEvent);

    List<GetCelestialEventDto> celestialEventListToGetDtoList(List<CelestialEvent> celestialEvents);

    GetCelestialEventImageDto celestialEventImageToGetDto(CelestialEventImage celestialEventImage);

    List<GetCelestialEventImageDto> celestialEventImageListToGetDtoList(List<CelestialEventImage> celestialEventImages);

    CelestialEvent createDtoToCelestialEvent(CreateCelestialEventDto createCelestialEventDto);

}
