package com.observatory.observationscheduler.domain.observation.dto;

import com.observatory.observationscheduler.domain.observation.models.Observation;
import com.observatory.observationscheduler.domain.observation.models.ObservationImage;
import com.observatory.observationscheduler.domain.observation.models.ObservationComment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ObservationDtoMapper {
    Observation createDtoToObservation(CreateObservationDto createObservationDto);

    GetObservationDto observationToGetDto(Observation observation);

    List<GetObservationDto> observationListToGetDtoList(List<Observation> observations);

    GetObservationImageDto observationImageToGetDto(ObservationImage observationImage);

    List<GetObservationImageDto> observationImageListToGetDtoList(List<ObservationImage> observationImages);

    ObservationComment createDtoToObservationComment(CreateObservationCommentDto comment);

    GetObservationCommentDto observationCommentToGetDto(ObservationComment comment);

    @Named("toSlimDto")
    GetSlimObservationDto observationToGetSlimDto(Observation observation);

    @IterableMapping(qualifiedByName = "toSlimDto")
    List<GetSlimObservationDto> observationListToGetSlimDtoList(List<Observation> observations);

}
