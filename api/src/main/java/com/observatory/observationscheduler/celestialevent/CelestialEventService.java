package com.observatory.observationscheduler.celestialevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.awsservice.S3Service;
import com.observatory.observationscheduler.awsservice.exceptions.InvalidImageException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventStatusNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.IncorrectCelestialEventFormatException;
import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import com.observatory.observationscheduler.configuration.JacksonConfig;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CelestialEventService {
    private final CelestialEventRepository celestialEventRepository;
    private final SpecificCelestialEventAssembler assembler;
    private final JacksonConfig jacksonConfig;
    private final S3Service s3Service;

    public CelestialEventService(CelestialEventRepository celestialEventRepository, SpecificCelestialEventAssembler assembler, JacksonConfig jacksonConfig, S3Service s3Service
    ) {
        this.celestialEventRepository = celestialEventRepository;
        this.assembler = assembler;
        this.jacksonConfig = jacksonConfig;
        this.s3Service = s3Service;
    }

    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getAllCelestialEvents() {
        List<CelestialEvent> allCelestialEvents = celestialEventRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(
                        assembler.toCollectionModel(allCelestialEvents),
                        linkTo(CelestialEventController.class).withSelfRel().withType("GET, POST"),
                        linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(null)).withRel("filter-by-status").withType("GET")
                ));

    }

    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEventsByStatus(CelestialEventStatus status) {
        List<CelestialEvent> events = celestialEventRepository.findCelestialEventByEventStatus(status).orElseThrow(() -> new CelestialEventStatusNotFoundException(status));
        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembler.toCollectionModel(events),
                        linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(null)).withSelfRel().withType("GET"),
                        linkTo(CelestialEventController.class).withRel("all").withType("GET, POST")
                )
        );
    }

    /*
     * Temporary way to batch update all celestial events that have already expired or completed
     */
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> updateCelestialEventStatus() {
        List<CelestialEvent> events = celestialEventRepository.findCelestialEventByEventStatus(CelestialEventStatus.UPCOMING).orElseThrow(() -> new CelestialEventStatusNotFoundException(CelestialEventStatus.UPCOMING));
        List<CelestialEvent> updatedEvents = events.stream().map(this::updateEventStatus).filter(Objects::nonNull).toList();

        return ResponseEntity.status(HttpStatus.OK).body(assembler.toCollectionModel(updatedEvents));
    }

    /*
     * Checks the DateTime of each celestial event and see if it is older than the current time, and mark it as completed if so.
     */
    private CelestialEvent updateEventStatus(CelestialEvent celestialEvent) {
        if (celestialEvent.getCelestialEventDateTime().isBefore(LocalDateTime.now()) && celestialEvent.getEventStatus() == CelestialEventStatus.UPCOMING) {
            celestialEvent.setEventStatus(CelestialEventStatus.COMPLETED);
            celestialEventRepository.saveAndFlush(celestialEvent);
            return celestialEvent;
        }
        return null;
    }

    public ResponseEntity<EntityModel<CelestialEvent>> getCelestialEventByUuid(String celestialEventUuid) {
        CelestialEvent event = celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
        Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(event).add(rootLink));
    }

    public ResponseEntity<EntityModel<CelestialEvent>> createCelestialEvent(CelestialEvent celestialEvent, List<MultipartFile> images) {
        try {
            List<String> imageUrls = images.stream().map(image -> {
                try {
                    return s3Service.uploadImage(image);
                } catch (InvalidImageException e) {
                    return null;
                }
            }).toList();

            List<CelestialEventImage> celestialEventImages = celestialEvent.convertImageToCelestialEventImage(imageUrls);
            celestialEvent.setImages(celestialEventImages);

            CelestialEvent createdEvent = celestialEventRepository.save(celestialEvent);
            Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(createdEvent).add(rootLink));
        } catch (RuntimeException e) {
            throw new IncorrectCelestialEventFormatException();
        }
    }

    public ResponseEntity<Void> deleteCelestialEvent(String celestialEventUuid) {
        CelestialEvent celestialEvent = celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));

        try {
            celestialEventRepository.delete(celestialEvent);
            celestialEvent.getImages().forEach(celestialEventImage -> s3Service.deleteImage(celestialEventImage.getUrl()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            throw new RuntimeException("There was an error in deletion");
        }


    }

    public ResponseEntity<EntityModel<CelestialEvent>> updateCelestialEvent(String celestialEventUuid, JsonPatch patch) {
        try {
            CelestialEvent celestialEvent = celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
            CelestialEvent updatedCelestialEvent = applyPatchToCelestialEvent(patch, celestialEvent);
            celestialEventRepository.save(updatedCelestialEvent);
            return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(updatedCelestialEvent));
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new IncorrectCelestialEventFormatException();
        }
    }

    private CelestialEvent applyPatchToCelestialEvent(JsonPatch patch, CelestialEvent celestialEvent) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = jacksonConfig.objectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(celestialEvent, JsonNode.class));
        return mapper.treeToValue(patched, CelestialEvent.class);
    }
}

@Component
class SpecificCelestialEventAssembler implements RepresentationModelAssembler<CelestialEvent, EntityModel<CelestialEvent>> {

    @Override
    public EntityModel<CelestialEvent> toModel(CelestialEvent celestialEvent) {
        return EntityModel.of(celestialEvent,
                linkTo(CelestialEventController.class).slash(celestialEvent.getUuid()).withSelfRel().withType("GET, PATCH, DELETE"));
    }

    @Override
    public CollectionModel<EntityModel<CelestialEvent>> toCollectionModel(Iterable<? extends CelestialEvent> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
