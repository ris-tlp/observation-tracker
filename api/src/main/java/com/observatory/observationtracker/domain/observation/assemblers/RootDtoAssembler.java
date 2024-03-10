package com.observatory.observationtracker.domain.observation.assemblers;

import com.observatory.observationtracker.domain.observation.ObservationController;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class RootDtoAssembler<T> {
    public EntityModel<T> toModel(T observation, String uuid) {
            return EntityModel.of(
                    observation,
                    linkTo(ObservationController.class).slash(uuid).withSelfRel().withType("GET, PATCH, DELETE")
//                linkTo(methodOn(ObservationController.class).getObservationByUuid(observation.getUuid(),
//                observation.getOwner().getUuid())).withSelfRel().withType("GET"),
//                linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(observation.getOwner()
//                .getUuid())).withRel("observations").withType("GET, POST"),
//                linkTo(methodOn(ObservationController.class).patchObservation(observation.getUuid(), null,
//                observation.getOwner().getUuid())).withRel("observation").withType("GET, PATCH, DELETE")
            );
        }
    }
