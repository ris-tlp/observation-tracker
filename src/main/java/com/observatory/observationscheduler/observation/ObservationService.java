package com.observatory.observationscheduler.observation;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ObservationService {
    public final ObservationRepository repository;
    public final ObservationAssembler assembler;

    public ObservationService(ObservationRepository repository, ObservationAssembler assembler) {
        this.assembler = assembler;
        this.repository = repository;
    }

    public CollectionModel<EntityModel<Observation>> getAllObservations() {
        CollectionModel<EntityModel<Observation>> assembledRequest = assembler.toCollectionModel(repository.findAll());
        return CollectionModel.of(
                assembledRequest,
                linkTo(methodOn(ObservationController.class).getAllObservations()).withSelfRel()
        );
    }

    public EntityModel<Observation> getObservationByUuid(String uuid) {
        return assembler.toModel(repository.findObservationByUuid(uuid).orElseThrow(() -> new ObservationNotFoundException(uuid)));

    }
}

@Component
class ObservationAssembler implements RepresentationModelAssembler<Observation, EntityModel<Observation>> {

    @Override
    public EntityModel<Observation> toModel(Observation observation) {
        return EntityModel.of(
                observation,
                linkTo(methodOn(ObservationController.class).getAllObservations()).withRel("observations")
        );
    }

    @Override
    public CollectionModel<EntityModel<Observation>> toCollectionModel(Iterable<? extends Observation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
