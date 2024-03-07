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
import com.observatory.observationtracker.rabbitmq.notifications.CommentNotificationProducer;
import com.observatory.observationtracker.rabbitmq.notifications.ReplyNotificationProducer;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ObservationService {
    private final ObservationRepository observationRepository;
    private final UserAccountRepository userRepository;
    private final CelestialEventRepository celestialEventRepository;
    private final ObservationCommentRepository commentRepository;

    private final Logger logger;
    private final S3Service s3Service;
    private final JacksonConfig jacksonConfig;
    private final ObservationDtoMapper dtoMapper;

    private final CommentNotificationProducer commentNotificationProducer;
    private final ReplyNotificationProducer replyNotificationProducer;


    public ObservationService(ObservationRepository observationRepository, UserAccountRepository userRepository,
                              S3Service s3Service,
                              CelestialEventRepository celestialEventRepository,
                              ObservationCommentRepository commentRepository,
                              Logger logger, JacksonConfig jacksonConfig,
                              ObservationDtoMapper dtoMapper,
                              CommentNotificationProducer commentNotificationProducer,
                              ReplyNotificationProducer replyNotificationProducer
    ) {
        this.userRepository = userRepository;
        this.observationRepository = observationRepository;
        this.s3Service = s3Service;
        this.celestialEventRepository = celestialEventRepository;
        this.logger = logger;
        this.jacksonConfig = jacksonConfig;
        this.dtoMapper = dtoMapper;
        this.commentRepository = commentRepository;
        this.commentNotificationProducer = commentNotificationProducer;
        this.replyNotificationProducer = replyNotificationProducer;
    }

    public Page<GetSlimObservationDto> getAllObservations(String userUuid,
                                                          Pageable pageable) {
        Page<GetSlimObservationDto> observationsDto =
                observationRepository.findByOwnerUuid(userUuid, pageable).map(dtoMapper::observationToGetSlimDto);

        return observationsDto;
    }

    @Cacheable(value = "singleObservation", key = "#observationUuid")
    public GetObservationDto getObservationByUuid(String observationUuid) {
        Observation observation =
                observationRepository.findByNullParentComment(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));

        GetObservationDto observationDto = dtoMapper.observationToGetDto(observation);

        return observationDto;
    }

    @Cacheable(value = "singleObservation")

    public GetObservationDto createObservation(CreateObservationDto newObservation,
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
                } catch (InvalidImageException exception) {
                    logger.warn(exception.getMessage());
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

            return observationDto;
        } catch (RuntimeException exception) {
            logger.error(exception.getMessage());
            throw new IncorrectObservationFormatException();
        }
    }

    // Used to patch specific fields of a PATCH request
    @Cacheable(value = "singleObservation", key = "#observationUuid")

    public GetObservationDto patchObservation(String observationUuid, JsonPatch patch) {
        try {
            Observation observation =
                    observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));
            Observation updatedObservation = applyPatchToObservation(patch, observation);


            observationRepository.save(updatedObservation);
            GetObservationDto observationDto = dtoMapper.observationToGetDto(updatedObservation);

            return observationDto;
        } catch (JsonPatchException | JsonProcessingException exception) {
            logger.error(exception.getMessage());
            throw new IncorrectObservationFormatException();
        }
    }

    private Observation applyPatchToObservation(JsonPatch patch, Observation targetObservation) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = jacksonConfig.objectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetObservation, JsonNode.class));
        return mapper.treeToValue(patched, Observation.class);
    }


    @CacheEvict(value = "singleObservation", key = "#observationUuid")
    public ResponseEntity<Void> deleteObservation(String observationUuid) {
        Observation observation =
                observationRepository.findObservationByUuid(observationUuid).orElseThrow(() -> new ObservationNotFoundException(observationUuid));

        try {
            observationRepository.delete(observation);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException exception) {
            logger.error(exception.getMessage());
            throw new RuntimeException("There was an error in deletion");
        }
    }

    public Page<GetSlimObservationDto> getPublishedCourses(Pageable pageable) {
        Page<GetSlimObservationDto> observationsDto =
                observationRepository.findObservationsByIsPublishedIsTrue(pageable).map(dtoMapper::observationToGetSlimDto);

        return observationsDto;
    }

    @Cacheable(value = "singleObservationComment")
    public GetObservationCommentDto addCommentToObservation(String observationUuid,
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

        GetObservationCommentDto commentDto = dtoMapper.observationCommentToGetDto(comment);

        // Add new notification to queue
        commentNotificationProducer.addObservationCommentMessage(
                dtoMapper.observationToGetDto(observation).getOwner(), commentDto);

        return commentDto;
    }

    @Cacheable(value = "singleObservationComment")
    public GetObservationCommentDto addReplyToObservation(String observationUuid, String userUuid,
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

        // Maintaining bidirectional relationships
        reply.setParentComment(parentComment);
        parentComment.getReplies().add(reply);

        commentRepository.save(reply);
        commentRepository.save(parentComment);

        GetObservationCommentDto commentDto = dtoMapper.observationCommentToGetDto(reply);

        // Add new notification to queue
        replyNotificationProducer.addReplyMessage(
                dtoMapper.observationCommentToGetDto(parentComment).getAuthor(),
                dtoMapper.observationCommentToGetDto(reply).getAuthor(),
                dtoMapper.observationCommentToGetDto(parentComment)
        );

        return commentDto;
    }
}
