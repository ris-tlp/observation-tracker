package com.observatory.observationscheduler.observation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.awsservice.S3Service;
import com.observatory.observationscheduler.awsservice.exceptions.InvalidImageException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationscheduler.configuration.JacksonConfig;
import com.observatory.observationscheduler.observation.exceptions.IncorrectObservationFormatException;
import com.observatory.observationscheduler.observation.exceptions.ObservationNotFoundException;
import com.observatory.observationscheduler.observation.models.Observation;
import com.observatory.observationscheduler.observation.models.ObservationImage;
import com.observatory.observationscheduler.observation.repositories.ObservationImageRepository;
import com.observatory.observationscheduler.observation.repositories.ObservationRepository;
import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import com.observatory.observationscheduler.useraccount.exceptions.UserNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final UserAccountRepository userRepository;
    private final CelestialEventRepository celestialEventRepository;
    private final ObservationImageRepository observationImageRepository;
    private final ObservationAssembler assembler;
    private final S3Service s3Service;
    private final JacksonConfig jacksonConfig;


    public ObservationService(ObservationRepository observationRepository, UserAccountRepository userRepository,
                              ObservationAssembler assembler, S3Service s3Service,
                              ObservationImageRepository observationImageRepository,
                              CelestialEventRepository celestialEventRepository,
                              JacksonConfig jacksonConfig) {
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.observationRepository = observationRepository;
        this.s3Service = s3Service;
        this.observationImageRepository = observationImageRepository;
        this.celestialEventRepository = celestialEventRepository;
        this.jacksonConfig = jacksonConfig;
    }

    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getAllObservations(String userUuid) {
        List<Observation> observations =
                observationRepository.findByOwnerUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
        CollectionModel<EntityModel<Observation>> assembledRequest = assembler.toCollectionModel(observations);

        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembledRequest,
                        linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(userUuid)).withSelfRel().withType("GET,  POST")
                )
        );
    }

    public ResponseEntity<EntityModel<Observation>> getObservationByUuid(String observationUuid) {
        Observation observation =
                observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
        Link rootLink =
                linkTo(ObservationController.class, observation.getOwner().getUuid()).withRel("all").withType("GET, " +
                        "POST");
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(observation).add(rootLink));
    }

    // @TODO: May need to be changed according to react frontend request test, check DTOs out
    public ResponseEntity<EntityModel<Observation>> createObservation(Observation newObservation, String userUuid,
                                                                      String celestialEventUuid,
                                                                      List<MultipartFile> images) {
        try {
            UserAccount user =
                    userRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));

            CelestialEvent event =
                    celestialEventRepository
                            .findCelestialEventByUuid(celestialEventUuid)
                            .orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));

            List<String> imageUrls = images.stream().map(image -> {
                try {
                    return s3Service.uploadImage(image);
                } catch (InvalidImageException e) {
                    return null;
                }
            }).toList();
            List<ObservationImage> observationImages = newObservation.convertImageToObservationImage(imageUrls);

            newObservation.setOwner(user);
            newObservation.setCelestialEvent(event);
            newObservation.setImages(observationImages);
            Link rootLink =
                    linkTo(ObservationController.class, newObservation.getOwner().getUuid()).withRel("all").withType(
                            "GET, POST");

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    assembler.toModel(observationRepository.save(newObservation)).add(rootLink));
        } catch (RuntimeException e) {
            throw new IncorrectObservationFormatException();
        }
    }

    // Used to patch specific fields of a PATCH request
    public ResponseEntity<EntityModel<Observation>> patchObservation(String uuid, JsonPatch patch) {
        try {
            Observation observation =
                    observationRepository.findObservationByUuid(uuid).orElseThrow(() -> new ObservationNotFoundException(uuid));
            Observation updatedObservation = applyPatchToObservation(patch, observation);
            observationRepository.save(updatedObservation);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(updatedObservation));
        } catch (JsonPatchException | JsonProcessingException exception) {
            System.out.println(exception);
            throw new IncorrectObservationFormatException();
        }
    }

    private Observation applyPatchToObservation(JsonPatch patch, Observation targetObservation) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = jacksonConfig.objectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetObservation, JsonNode.class));
        return mapper.treeToValue(patched, Observation.class);
    }


    public ResponseEntity<Void> deleteObservation(String observationUuid) {
        Observation observation =
                observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));

        try {
            observationRepository.delete(observation);
            observation.getImages().forEach(observationImage -> s3Service.deleteImage(observationImage.getUrl()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            throw new RuntimeException("There was an error in deletion");
        }
    }

    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getPublishedCourses() {
        List<Observation> observations =
                observationRepository.findObservationsByIsPublishedIsTrue().orElseThrow();

        CollectionModel<EntityModel<Observation>> assembledRequest = assembler.toCollectionModel(observations);

        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembledRequest,
                        linkTo(methodOn(ObservationController.class).getPublishedObservations()).withSelfRel()
//                        linkTo(methodOn().withSelfRel().withType("GET,  POST")
                )
        );
    }
}

@Component
class ObservationAssembler implements RepresentationModelAssembler<Observation, EntityModel<Observation>> {

    @Override
    public EntityModel<Observation> toModel(Observation observation) {
        return EntityModel.of(
                observation,
                linkTo(ObservationController.class).slash(observation.getUuid()).withSelfRel().withType("GET, PATCH, " +
                        "DELETE")
//                linkTo(methodOn(ObservationController.class).getObservationByUuid(observation.getUuid(),
//                observation.getOwner().getUuid())).withSelfRel().withType("GET"),
//                linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(observation.getOwner()
//                .getUuid())).withRel("observations").withType("GET, POST"),
//                linkTo(methodOn(ObservationController.class).patchObservation(observation.getUuid(), null,
//                observation.getOwner().getUuid())).withRel("observation").withType("GET, PATCH, DELETE")
        );
    }

    @Override
    public CollectionModel<EntityModel<Observation>> toCollectionModel(Iterable<? extends Observation> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
