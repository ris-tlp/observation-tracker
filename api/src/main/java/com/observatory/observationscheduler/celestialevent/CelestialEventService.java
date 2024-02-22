package com.observatory.observationscheduler.celestialevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.aws.S3Service;
import com.observatory.observationscheduler.aws.exceptions.InvalidImageException;
import com.observatory.observationscheduler.celestialevent.assemblers.CelestialEventDtoAssembler;
import com.observatory.observationscheduler.celestialevent.assemblers.CelestialEventSlimDtoAssembler;
import com.observatory.observationscheduler.celestialevent.dto.*;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventCommentNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventStatusNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.IncorrectCelestialEventFormatException;
import com.observatory.observationscheduler.celestialevent.models.*;
import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventCommentRepository;
import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventImageRepository;
import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationscheduler.configuration.JacksonConfig;
import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


// @TODO all fetch showign replies
@Service
public class CelestialEventService {
    private final CelestialEventRepository celestialEventRepository;
    private final CelestialEventImageRepository celestialEventImageRepository;
    private final CelestialEventDtoAssembler dtoAssembler;
    private final CelestialEventSlimDtoAssembler slimDtoAssembler;
    private final UserAccountRepository userAccountRepository;
    private final CelestialEventCommentRepository celestialEventCommentRepository;

    private final JacksonConfig jacksonConfig;
    private final S3Service s3Service;
    private final CelestialEventDtoMapper celestialEventDtoMapper;

    public CelestialEventService(CelestialEventRepository celestialEventRepository, CelestialEventDtoAssembler dtoAssembler
            , JacksonConfig jacksonConfig, S3Service s3Service,
                                 CelestialEventDtoMapper celestialEventDtoMapper,
                                 CelestialEventImageRepository celestialEventImageRepository,
                                 UserAccountRepository userAccountRepository,
                                 CelestialEventCommentRepository celestialEventCommentRepository, CelestialEventSlimDtoAssembler slimDtoAssembler
    ) {
        this.celestialEventRepository = celestialEventRepository;
        this.dtoAssembler = dtoAssembler;
        this.jacksonConfig = jacksonConfig;
        this.s3Service = s3Service;
        this.celestialEventImageRepository = celestialEventImageRepository;
        this.celestialEventDtoMapper = celestialEventDtoMapper;
        this.userAccountRepository = userAccountRepository;
        this.celestialEventCommentRepository = celestialEventCommentRepository;
        this.slimDtoAssembler = slimDtoAssembler;
    }

    public ResponseEntity<CollectionModel<EntityModel<GetSlimCelestialEventDto>>> getAllCelestialEvents() {
        List<CelestialEvent> allCelestialEvents = celestialEventRepository.findAll();
        List<GetSlimCelestialEventDto> celestialEventDtos =
                celestialEventDtoMapper.celestialEventListToGetSlimDtoList(allCelestialEvents);


        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(
                        slimDtoAssembler.toCollectionModel(celestialEventDtos),
                        linkTo(CelestialEventController.class).withSelfRel().withType("GET, POST"),
                        linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(null)).withRel(
                                "filter-by-status").withType("GET")
                ));

    }

    public ResponseEntity<CollectionModel<EntityModel<GetSlimCelestialEventDto>>> getCelestialEventsByStatus(CelestialEventStatus status) {
        List<CelestialEvent> events =
                celestialEventRepository.findCelestialEventByEventStatus(status).orElseThrow(() -> new CelestialEventStatusNotFoundException(status));
        List<GetSlimCelestialEventDto> celestialEventDtos = celestialEventDtoMapper.celestialEventListToGetSlimDtoList(events);
        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        slimDtoAssembler.toCollectionModel(celestialEventDtos),
                        linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(null)).withSelfRel().withType("GET"),
                        linkTo(CelestialEventController.class).withRel("all").withType("GET, POST")
                )
        );
    }

    /*
     * Temporary way to batch update all celestial events that have already expired or completed
     */
    public ResponseEntity<Void> updateCelestialEventStatus() {
        List<CelestialEvent> events =
                celestialEventRepository.findCelestialEventByEventStatus(CelestialEventStatus.UPCOMING).orElseThrow(() -> new CelestialEventStatusNotFoundException(CelestialEventStatus.UPCOMING));
        List<CelestialEvent> updatedEvents =
                events.stream().map(this::updateEventStatus).filter(Objects::nonNull).toList();

        return ResponseEntity.status(HttpStatus.OK).build();

//        return null;
    }

    /*
     * Checks the DateTime of each celestial event and see if it is older than the current time, and mark it as
     * completed if so.
     */
    private CelestialEvent updateEventStatus(CelestialEvent celestialEvent) {
        if (celestialEvent.getCelestialEventDateTime().isBefore(LocalDateTime.now()) && celestialEvent.getEventStatus() == CelestialEventStatus.UPCOMING) {
            celestialEvent.setEventStatus(CelestialEventStatus.COMPLETED);
            celestialEventRepository.saveAndFlush(celestialEvent);
            return celestialEvent;
        }
        return null;
    }

    public ResponseEntity<EntityModel<GetCelestialEventDto>> getCelestialEventByUuid(String celestialEventUuid) {
        CelestialEvent event = celestialEventRepository.findByNullParentComment(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));

        GetCelestialEventDto celestialEventDto = celestialEventDtoMapper.celestialEventToGetDto(event);
        Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");

        return ResponseEntity.status(HttpStatus.OK).body(dtoAssembler.toModel(celestialEventDto).add(rootLink));
    }

    public ResponseEntity<EntityModel<GetCelestialEventDto>> createCelestialEvent(CreateCelestialEventDto celestialEvent,
                                                                                  List<MultipartFile> images) {
        try {
            List<String> imageUrls = images.stream().map(image -> {
                try {
                    return s3Service.uploadImage(image);
                } catch (InvalidImageException e) {
                    return null;
                }
            }).toList();

            CelestialEvent newCelestialEventEntity = celestialEventDtoMapper.createDtoToCelestialEvent(celestialEvent);
            List<CelestialEventImage> celestialEventImages =
                    newCelestialEventEntity.convertImageToCelestialEventImage(imageUrls);
            newCelestialEventEntity.setImages(celestialEventImages);

            newCelestialEventEntity = celestialEventRepository.save(newCelestialEventEntity);
            GetCelestialEventDto createdEvent = celestialEventDtoMapper.celestialEventToGetDto(newCelestialEventEntity);

            Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoAssembler.toModel(createdEvent).add(rootLink));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new IncorrectCelestialEventFormatException();
        }
    }

    public ResponseEntity<Void> deleteCelestialEvent(String celestialEventUuid) {
        CelestialEvent celestialEvent =
                celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));

        try {
            celestialEventRepository.delete(celestialEvent);
            celestialEvent.getImages().forEach(celestialEventImage -> s3Service.deleteImage(celestialEventImage.getUrl()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            throw new RuntimeException("There was an error in deletion");
        }


    }

    public ResponseEntity<EntityModel<GetCelestialEventDto>> updateCelestialEvent(String celestialEventUuid,
                                                                                  JsonPatch patch) {
        try {
            CelestialEvent celestialEvent =
                    celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
            CelestialEvent updatedCelestialEvent = applyPatchToCelestialEvent(patch, celestialEvent);
            celestialEventRepository.save(updatedCelestialEvent);

            GetCelestialEventDto celestialEventDto =
                    celestialEventDtoMapper.celestialEventToGetDto(updatedCelestialEvent);

            return ResponseEntity.status(HttpStatus.OK).body(dtoAssembler.toModel(celestialEventDto));
        } catch (JsonPatchException | JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            throw new IncorrectCelestialEventFormatException();
        }
    }

    private CelestialEvent applyPatchToCelestialEvent(JsonPatch patch, CelestialEvent celestialEvent) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = jacksonConfig.objectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(celestialEvent, JsonNode.class));
        return mapper.treeToValue(patched, CelestialEvent.class);
    }


    public ResponseEntity<GetCelestialEventCommentDto> addCommentToCelestialEvent(String celestialEventUuid,
                                                                                  String userUuid,
                                                                                  CreateCelestialEventCommentDto newComment) {
        CelestialEvent celestialEvent =
                celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
        UserAccount author =
                userAccountRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(userUuid));

        CelestialEventComment celestialEventComment =
                celestialEventDtoMapper.createDtoToCelestialEventComment(newComment);

        celestialEventComment.setAuthor(author);

        // Maintaining bidirectional relationship according to JPA spec
        celestialEventComment.setCelestialEvent(celestialEvent);
        celestialEvent.getComments().add(celestialEventComment);

        celestialEventCommentRepository.save(celestialEventComment);
        celestialEventRepository.save(celestialEvent);

        GetCelestialEventCommentDto returnDto =
                celestialEventDtoMapper.celestialEventCommentToGetDto(celestialEventComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                returnDto
        );
    }

    public ResponseEntity<GetSlimCelestialEventCommentDto> addReplyToCelestialEventComment(String celestialEventUuid,
                                                                                           String userUuid,
                                                                                           String parentCommentUuid,
                                                                                           CreateCelestialEventCommentDto newComment) {
        // custom exception
        CelestialEventComment parentComment =
                celestialEventCommentRepository.findCelestialEventCommentByUuid(parentCommentUuid).orElseThrow(() -> new CelestialEventCommentNotFoundException(parentCommentUuid));
        CelestialEvent celestialEvent =
                celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
        UserAccount author =
                userAccountRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(userUuid));

        CelestialEventComment celestialEventReply =
                celestialEventDtoMapper.createDtoToCelestialEventComment(newComment);

        celestialEventReply.setAuthor(author);
        celestialEventReply.setCelestialEvent(celestialEvent);

        // Maintaining bidirectional relationship according to JPA spec
        celestialEventReply.setParentComment(parentComment);
        parentComment.getReplies().add(celestialEventReply);

        celestialEventCommentRepository.save(celestialEventReply);
        celestialEventCommentRepository.save(parentComment);

        GetSlimCelestialEventCommentDto returnDto =
                celestialEventDtoMapper.celestialEventReplyToGetSlimDto(celestialEventReply);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }
}




