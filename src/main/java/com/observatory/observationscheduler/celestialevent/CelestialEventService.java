package com.observatory.observationscheduler.celestialevent;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CelestialEventService {
    private final CelestialEventRepository celestialEventRepository;
    private final CelestialEventAssembler assembler;

    public CelestialEventService(CelestialEventRepository celestialEventRepository, CelestialEventAssembler assembler) {
        this.celestialEventRepository = celestialEventRepository;
        this.assembler = assembler;
    }

    public CollectionModel<EntityModel<CelestialEvent>> getAllCelestialEvents() {
        return assembler.toCollectionModel(celestialEventRepository.findAll());
    }
}

@Component
class CelestialEventAssembler implements RepresentationModelAssembler<CelestialEvent, EntityModel<CelestialEvent>> {

    @Override
    public EntityModel<CelestialEvent> toModel(CelestialEvent celestialEvent) {
        return EntityModel.of(
                celestialEvent,
                linkTo(methodOn(CelestialEventController.class).getAllCelestialEvents()).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<CelestialEvent>> toCollectionModel(Iterable<? extends CelestialEvent> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
