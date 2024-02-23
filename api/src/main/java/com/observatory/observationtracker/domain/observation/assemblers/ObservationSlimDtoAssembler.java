package com.observatory.observationtracker.domain.observation.assemblers;

import com.observatory.observationtracker.domain.observation.dto.GetSlimObservationDto;
import com.observatory.observationtracker.domain.celestialevent.assemblers.RootDtoAssembler;
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


