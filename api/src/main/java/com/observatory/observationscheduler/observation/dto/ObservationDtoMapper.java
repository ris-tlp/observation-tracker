package com.observatory.observationscheduler.observation.dto;

import com.observatory.observationscheduler.observation.models.Observation;
import com.observatory.observationscheduler.observation.models.ObservationImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ObservationDtoMapper {
    Observation createDtoToObservation(CreateObservationDto createObservationDto);

    GetObservationDto observationToGetDto(Observation observation);

    List<GetObservationDto> observationListToGetDtoList(List<Observation> observations);

    GetObservationImageDto observationImageToGetDto(ObservationImage observationImage);

    List<GetObservationImageDto> observationImageListToGetDtoList(List<ObservationImage> observationImages);
}
