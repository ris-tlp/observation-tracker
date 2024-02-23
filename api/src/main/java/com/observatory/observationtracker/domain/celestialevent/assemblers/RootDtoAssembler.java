package com.observatory.observationtracker.domain.celestialevent.assemblers;

import com.observatory.observationtracker.domain.celestialevent.CelestialEventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class RootDtoAssembler<T> {
    public EntityModel<T> toModel(T celestialEvent, String uuid) {
        return EntityModel.of(
                celestialEvent,
                WebMvcLinkBuilder.linkTo(methodOn(CelestialEventController.class).getCelestialEventByUuid(uuid)).withSelfRel().withType("GET, PATCH, DELETE")
                        .andAffordance(afford(methodOn(CelestialEventController.class).deleteCelestialEvent(uuid)))
                        .andAffordance(afford(methodOn(CelestialEventController.class).updateCelestialEvent(uuid, null))));

    }
}
