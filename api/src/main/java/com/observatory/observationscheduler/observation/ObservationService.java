package com.observatory.observationscheduler.observation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.celestialevent.CelestialEventController;
import com.observatory.observationscheduler.observation.exceptions.IncorrectObservationFormatException;
import com.observatory.observationscheduler.observation.exceptions.ObservationNotFoundException;
import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import com.observatory.observationscheduler.useraccount.exceptions.UserNotFoundException;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final UserAccountRepository userRepository;
    private final ObservationAssembler assembler;
    private static final Logger log = LoggerFactory.getLogger(ObservationService.class);


    public ObservationService(ObservationRepository observationRepository, UserAccountRepository userRepository, ObservationAssembler assembler) {
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.observationRepository = observationRepository;
    }

    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getAllObservations(String userUuid) {
        List<Observation> observations = observationRepository.findByOwnerUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
        CollectionModel<EntityModel<Observation>> assembledRequest = assembler.toCollectionModel(observations);

        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembledRequest,
                        linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(userUuid)).withSelfRel().withType("GET,  POST")
                )
        );
    }

    public ResponseEntity<EntityModel<Observation>> getObservationByUuid(String observationUuid, String userUuid) {
        Observation observation = observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
        Link rootLink = linkTo(ObservationController.class, observation.getOwner().getUuid()).withRel("all").withType("GET, POST");
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(observation).add(rootLink));
    }

    public ResponseEntity<EntityModel<Observation>> createObservation(Observation newObservation, String userUuid) {
        try {
            UserAccount user = userRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
            newObservation.setOwner(user);
            Link rootLink = linkTo(ObservationController.class, newObservation.getOwner().getUuid()).withRel("all").withType("GET, POST");

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    assembler.toModel(observationRepository.save(newObservation)).add(rootLink));
        } catch (RuntimeException e) {
            throw new IncorrectObservationFormatException();
        }
    }

    // Used to patch specific fields of a PATCH request
    public ResponseEntity<EntityModel<Observation>> patchObservation(String uuid, JsonPatch patch) {
        try {
            Observation observation = observationRepository.findObservationByUuid(uuid).orElseThrow(() -> new ObservationNotFoundException(uuid));
            Observation updatedObservation = applyPatchToObservation(patch, observation);
            observationRepository.save(updatedObservation);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(updatedObservation));
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new IncorrectObservationFormatException();
        }
    }

    private Observation applyPatchToObservation(JsonPatch patch, Observation targetObservation) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetObservation, JsonNode.class));
        return mapper.treeToValue(patched, Observation.class);
    }


    public ResponseEntity<?> deleteObservation(String userUuid, String observationUuid) {
        Observation observation = observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
        observationRepository.delete(observation);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

@Component
class ObservationAssembler implements RepresentationModelAssembler<Observation, EntityModel<Observation>> {

    @Override
    public EntityModel<Observation> toModel(Observation observation) {
        return EntityModel.of(
                observation,
                linkTo(ObservationController.class, observation.getOwner().getUuid()).slash(observation.getUuid()).withSelfRel().withType("GET, PATCH, DELETE")
//                linkTo(methodOn(ObservationController.class).getObservationByUuid(observation.getUuid(), observation.getOwner().getUuid())).withSelfRel().withType("GET"),
//                linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(observation.getOwner().getUuid())).withRel("observations").withType("GET, POST"),
//                linkTo(methodOn(ObservationController.class).patchObservation(observation.getUuid(), null, observation.getOwner().getUuid())).withRel("observation").withType("GET, PATCH, DELETE")
        );
    }

    @Override
    public CollectionModel<EntityModel<Observation>> toCollectionModel(Iterable<? extends Observation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
