package com.observatory.observationscheduler.celestialevent.assemblers;

import com.observatory.observationscheduler.celestialevent.dto.GetCelestialEventDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class CelestialEventDtoAssembler extends RootDtoAssembler<GetCelestialEventDto> implements RepresentationModelAssembler<GetCelestialEventDto,
        EntityModel<GetCelestialEventDto>> {

    @Override
    public EntityModel<GetCelestialEventDto> toModel(GetCelestialEventDto entity) {
        return super.toModel(entity, entity.getUuid());
    }

    @Override
    public CollectionModel<EntityModel<GetCelestialEventDto>> toCollectionModel(Iterable<? extends GetCelestialEventDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}