package com.observatory.observationtracker.domain.observation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationtracker.aws.S3Service;
import com.observatory.observationtracker.aws.exceptions.InvalidImageException;
import com.observatory.observationtracker.configuration.JacksonConfig;
import com.observatory.observationtracker.domain.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationtracker.domain.observation.assemblers.ObservationDtoAssembler;
import com.observatory.observationtracker.domain.observation.assemblers.ObservationSlimDtoAssembler;
import com.observatory.observationtracker.domain.observation.dto.*;
import com.observatory.observationtracker.domain.observation.exceptions.IncorrectObservationFormatException;
import com.observatory.observationtracker.domain.observation.exceptions.ObservationNotFoundException;
import com.observatory.observationtracker.domain.observation.models.Observation;
import com.observatory.observationtracker.domain.observation.models.ObservationComment;
import com.observatory.observationtracker.domain.observation.models.ObservationImage;
import com.observatory.observationtracker.domain.observation.repositories.ObservationCommentRepository;
import com.observatory.observationtracker.domain.observation.repositories.ObservationRepository;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import com.observatory.observationtracker.domain.useraccount.UserAccountRepository;
import com.observatory.observationtracker.domain.useraccount.exceptions.UserNotFoundException;
import com.observatory.observationtracker.rabbitmq.notifications.NotificationProducer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final UserAccountRepository userRepository;
    private final CelestialEventRepository celestialEventRepository;
    private final ObservationDtoAssembler observationDtoAssembler;
    private final ObservationSlimDtoAssembler observationSlimDtoAssembler;
    private final ObservationCommentRepository commentRepository;

    private final S3Service s3Service;
    private final JacksonConfig jacksonConfig;
    private final ObservationDtoMapper dtoMapper;
    private final NotificationProducer notificationProducer;


    public ObservationService(ObservationRepository observationRepository, UserAccountRepository userRepository,
                              ObservationDtoAssembler observationDtoAssembler, S3Service s3Service,
                              ObservationSlimDtoAssembler observationSlimDtoAssembler,
                              CelestialEventRepository celestialEventRepository,
                              ObservationCommentRepository commentRepository,
                              JacksonConfig jacksonConfig,
                              ObservationDtoMapper dtoMapper,
                              NotificationProducer notificationProducer) {
        this.observationDtoAssembler = observationDtoAssembler;
        this.userRepository = userRepository;
        this.observationRepository = observationRepository;
        this.s3Service = s3Service;
        this.celestialEventRepository = celestialEventRepository;
        this.jacksonConfig = jacksonConfig;
        this.dtoMapper = dtoMapper;
        this.commentRepository = commentRepository;
        this.observationSlimDtoAssembler = observationSlimDtoAssembler;
        this.notificationProducer = notificationProducer;
    }

    public ResponseEntity<CollectionModel<EntityModel<GetSlimObservationDto>>> getAllObservations(String userUuid) {
        List<Observation> observations =
                observationRepository.findByOwnerUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));

        List<GetSlimObservationDto> observationDtos = dtoMapper.observationListToGetSlimDtoList(observations);
        CollectionModel<EntityModel<GetSlimObservationDto>> assembledRequest =
                observationSlimDtoAssembler.toCollectionModel(observationDtos);

        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembledRequest,
                        linkTo(methodOn(ObservationController.class).getAllObservationsOfUser(userUuid)).withSelfRel().withType("GET,  POST")
                )
        );
    }

    public ResponseEntity<EntityModel<GetObservationDto>> getObservationByUuid(String observationUuid) {
        Observation observation =
                observationRepository.findByNullParentComment(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));

        GetObservationDto observationDto = dtoMapper.observationToGetDto(observation);
        Link rootLink =
                linkTo(ObservationController.class, observation.getOwner().getUuid()).withRel("all").withType("GET, " +
                        "POST");

        return ResponseEntity.status(HttpStatus.OK).body(observationDtoAssembler.toModel(observationDto).add(rootLink));
    }

    // @TODO: May need to be changed according to react frontend request test, check DTOs out
    public ResponseEntity<EntityModel<GetObservationDto>> createObservation(CreateObservationDto newObservation,
                                                                            String userUuid,
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

            Observation newObservationEntity = dtoMapper.createDtoToObservation(newObservation);
            List<ObservationImage> observationImages = newObservationEntity.convertImageToObservationImage(imageUrls);

            // Set detached entities
            newObservationEntity.setOwner(user);
            newObservationEntity.setCelestialEvent(event);
            newObservationEntity.setImages(observationImages);

            Observation createdObservationEntity = observationRepository.save(newObservationEntity);
            GetObservationDto observationDto = dtoMapper.observationToGetDto(createdObservationEntity);

            Link rootLink =
                    linkTo(ObservationController.class, newObservationEntity.getOwner().getUuid()).withRel("all").withType(
                            "GET, POST");

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    observationDtoAssembler.toModel(observationDto).add(rootLink));
        } catch (RuntimeException e) {
            throw new IncorrectObservationFormatException();
        }
    }

    // Used to patch specific fields of a PATCH request
    public ResponseEntity<EntityModel<GetObservationDto>> patchObservation(String uuid, JsonPatch patch) {
        try {
            Observation observation =
                    observationRepository.findObservationByUuid(uuid).orElseThrow(() -> new ObservationNotFoundException(uuid));
            Observation updatedObservation = applyPatchToObservation(patch, observation);


            observationRepository.save(updatedObservation);
            GetObservationDto observationDto = dtoMapper.observationToGetDto(updatedObservation);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    observationDtoAssembler.toModel(observationDto)
            );
        } catch (JsonPatchException | JsonProcessingException exception) {
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
//            observation.getImages().forEach(observationImage -> s3Service.deleteImage(observationImage.getUrl()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            throw new RuntimeException("There was an error in deletion");
        }
    }

    public ResponseEntity<CollectionModel<EntityModel<GetSlimObservationDto>>> getPublishedCourses() {
        List<Observation> observations =
                observationRepository.findObservationsByIsPublishedIsTrue().orElseThrow();

        List<GetSlimObservationDto> observationDtos = dtoMapper.observationListToGetSlimDtoList(observations);

        CollectionModel<EntityModel<GetSlimObservationDto>> assembledRequest =
                observationSlimDtoAssembler.toCollectionModel(observationDtos);

        return ResponseEntity.status(HttpStatus.OK).body(
                CollectionModel.of(
                        assembledRequest,
                        linkTo(methodOn(ObservationController.class).getPublishedObservations()).withSelfRel()
//                        linkTo(methodOn().withSelfRel().withType("GET,  POST")
                )
        );
    }

    public ResponseEntity<GetObservationCommentDto> addCommentToObservation(String observationUuid,
                                                                            String userUuid,
                                                                            CreateObservationCommentDto newComment) {
        Observation observation =
                observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
        UserAccount author =
                userRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));

        ObservationComment comment = dtoMapper.createDtoToObservationComment(newComment);
        comment.setAuthor(author);
        // Maintaining bidirectional relationship of comments and observations
        comment.setObservation(observation);
        observation.getComments().add(comment);

        commentRepository.save(comment);
        observationRepository.save(observation);

        GetObservationCommentDto returnDto = dtoMapper.observationCommentToGetDto(comment);

        notificationProducer.sendObservationCommentMessage(
                dtoMapper.observationToGetDto(observation).getOwner(), returnDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }

    public ResponseEntity<GetObservationCommentDto> addReplyToObservation(String observationUuid, String userUuid,
                                                                          String parentCommentUuid,
                                                                          CreateObservationCommentDto newReply) {
        Observation observation =
                observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
        UserAccount author =
                userRepository.findUserAccountByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
        ObservationComment parentComment =
                commentRepository.findObservationCommentByUuid(parentCommentUuid).orElseThrow(() -> new ObservationNotFoundException(parentCommentUuid));
        ObservationComment reply = dtoMapper.createDtoToObservationComment(newReply);

        reply.setAuthor(author);
        reply.setObservation(observation);

        // Maintaining bidirectional relationship3
        reply.setParentComment(parentComment);
        parentComment.getReplies().add(reply);

        commentRepository.save(reply);
        commentRepository.save(parentComment);

        GetObservationCommentDto returnDto = dtoMapper.observationCommentToGetDto(reply);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }
}
