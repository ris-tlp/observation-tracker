package com.observatory.observationscheduler.observation.assemblers;

import com.observatory.observationscheduler.celestialevent.assemblers.RootDtoAssembler;
import com.observatory.observationscheduler.observation.dto.GetObservationDto;
import com.observatory.observationscheduler.observation.dto.GetSlimObservationDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ObservationSlimDtoAssembler extends RootDtoAssembler<GetSlimObservationDto> implements RepresentationModelAssembler<GetSlimObservationDto, EntityModel<GetSlimObservationDto>> {
    @Override
    public EntityModel<GetSlimObservationDto> toModel(GetSlimObservationDto entity) {
        return super.toModel(entity, entity.getUuid());
    }

    @Override
    public CollectionModel<EntityModel<GetSlimObservationDto>> toCollectionModel(Iterable<? extends GetSlimObservationDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}


