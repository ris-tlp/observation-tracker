package com.observatory.observationscheduler.observation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.startup.DataInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ObservationService {
    public final ObservationRepository repository;
    public final ObservationAssembler assembler;
    private static final Logger log = LoggerFactory.getLogger(ObservationService.class);


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

    public ResponseEntity<EntityModel<Observation>> patchObservation(String uuid, JsonPatch patch) {

        try {
            Observation observation = repository.findObservationByUuid(uuid).orElseThrow(() -> new ObservationNotFoundException(uuid));
            Observation updatedObservation = applyPatchToObservation(patch, observation);
            repository.saveAndFlush(updatedObservation);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(updatedObservation));
        } catch (JsonPatchException | JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * Use JsonPatch to only update the fields passed in the PATCH request
     * */
    public Observation applyPatchToObservation(JsonPatch patch, Observation targetObservation) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetObservation, JsonNode.class));
        return mapper.treeToValue(patched, Observation.class);
    }


}

@Component
class ObservationAssembler implements RepresentationModelAssembler<Observation, EntityModel<Observation>> {

    @Override
    public EntityModel<Observation> toModel(Observation observation) {
        return EntityModel.of(
                observation,
                linkTo(methodOn(ObservationController.class).getObservationByUuid(observation.getUuid())).withSelfRel(),
                linkTo(methodOn(ObservationController.class).getAllObservations()).withRel("observations")
        );
    }

    @Override
    public CollectionModel<EntityModel<Observation>> toCollectionModel(Iterable<? extends Observation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
