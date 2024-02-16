package com.observatory.observationscheduler.observation.dto;

import com.observatory.observationscheduler.observation.models.Observation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ObservationDtoMapper {
    Observation createDtoToObservation(CreateObservationDto createObservationDto);

    GetObservationDto observationToGetDto(Observation observation);

    List<GetObservationDto> observationListToGetDtoList(List<Observation> observations);
}
