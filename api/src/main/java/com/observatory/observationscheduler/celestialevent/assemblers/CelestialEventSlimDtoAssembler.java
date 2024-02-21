package com.observatory.observationscheduler.celestialevent.assemblers;

import com.observatory.observationscheduler.celestialevent.dto.GetSlimCelestialEventDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CelestialEventSlimDtoAssembler extends RootDtoAssembler<GetSlimCelestialEventDto> implements RepresentationModelAssembler<GetSlimCelestialEventDto,
        EntityModel<GetSlimCelestialEventDto>> {

    @Override
    public EntityModel<GetSlimCelestialEventDto> toModel(GetSlimCelestialEventDto entity) {
        return super.toModel(entity, entity.getUuid());
    }

    @Override
    public CollectionModel<EntityModel<GetSlimCelestialEventDto>> toCollectionModel(Iterable<? extends GetSlimCelestialEventDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
