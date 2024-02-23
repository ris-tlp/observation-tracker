package com.observatory.observationscheduler.observation.assemblers;

import com.observatory.observationscheduler.celestialevent.assemblers.RootDtoAssembler;
import com.observatory.observationscheduler.observation.dto.GetObservationDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ObservationDtoAssembler  extends RootDtoAssembler<GetObservationDto> implements RepresentationModelAssembler<GetObservationDto,
        EntityModel<GetObservationDto>> {
    @Override
    public EntityModel<GetObservationDto> toModel(GetObservationDto entity) {
        return super.toModel(entity, entity.getUuid());
    }

    @Override
    public CollectionModel<EntityModel<GetObservationDto>> toCollectionModel(Iterable<? extends GetObservationDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
