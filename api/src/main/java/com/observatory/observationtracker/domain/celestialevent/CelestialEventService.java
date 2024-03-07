package com.observatory.observationtracker.domain.celestialevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationtracker.aws.S3Service;
import com.observatory.observationtracker.aws.exceptions.InvalidImageException;
import com.observatory.observationtracker.configuration.JacksonConfig;
import com.observatory.observationtracker.domain.celestialevent.dto.*;
import com.observatory.observationtracker.domain.celestialevent.exceptions.CelestialEventCommentNotFoundException;
import com.observatory.observationtracker.domain.celestialevent.exceptions.CelestialEventStatusNotFoundException;
import com.observatory.observationtracker.domain.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationtracker.domain.celestialevent.exceptions.IncorrectCelestialEventFormatException;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventComment;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventImage;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;
import com.observatory.observationtracker.domain.celestialevent.repositories.CelestialEventCommentRepository;
import com.observatory.observationtracker.domain.celestialevent.repositories.CelestialEventImageRepository;
import com.observatory.observationtracker.domain.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import com.observatory.observationtracker.domain.useraccount.UserAccountRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


// @TODO all fetch showing replies
@Service
public class CelestialEventService {
    private final CelestialEventRepository celestialEventRepository;
    private final CelestialEventImageRepository celestialEventImageRepository;
    private final UserAccountRepository userAccountRepository;
    private final CelestialEventCommentRepository celestialEventCommentRepository;


    private final JacksonConfig jacksonConfig;
    private final S3Service s3Service;
    private final CelestialEventDtoMapper celestialEventDtoMapper;

    public CelestialEventService(CelestialEventRepository celestialEventRepository,
                                 JacksonConfig jacksonConfig,
                                 S3Service s3Service, CelestialEventDtoMapper celestialEventDtoMapper,
                                 CelestialEventImageRepository celestialEventImageRepository,
                                 UserAccountRepository userAccountRepository,
                                 CelestialEventCommentRepository celestialEventCommentRepository
    ) {
        this.celestialEventRepository = celestialEventRepository;
        this.jacksonConfig = jacksonConfig;
        this.s3Service = s3Service;
        this.celestialEventImageRepository = celestialEventImageRepository;
        this.celestialEventDtoMapper = celestialEventDtoMapper;
        this.userAccountRepository = userAccountRepository;
        this.celestialEventCommentRepository = celestialEventCommentRepository;
    }

    public Page<GetSlimCelestialEventDto> getAllCelestialEvents(Pageable pageable) {
        Page<GetSlimCelestialEventDto> pagedCelestialEvents =
                celestialEventRepository.findAll(pageable).map(celestialEventDtoMapper::celestialEventToGetSlimDto);

        return pagedCelestialEvents;
    }

    public Page<GetSlimCelestialEventDto> getCelestialEventsByStatus(CelestialEventStatus status, Pageable pageable) {
        Page<GetSlimCelestialEventDto> eventsDtos =
                celestialEventRepository.findPagedCelestialEventByEventStatus(status, pageable)
                        .orElseThrow(() -> new CelestialEventStatusNotFoundException(status))
                        .map(celestialEventDtoMapper::celestialEventToGetSlimDto);

        return eventsDtos;
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

    @Cacheable(value = "singleCelestialEvent", key = "#celestialEventUuid")
    public GetCelestialEventDto getCelestialEventByUuid(String celestialEventUuid) {
        CelestialEvent event =
                celestialEventRepository.findByNullParentComment(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));

        GetCelestialEventDto eventDto = celestialEventDtoMapper.celestialEventToGetDto(event);
        return eventDto;

    }

    @Cacheable(value = "singleCelestialEvent")
    public GetCelestialEventDto createCelestialEvent(CreateCelestialEventDto celestialEvent,
                                                     List<MultipartFile> images) {
        try {
            List<String> imageUrls = images.stream().map(image -> {
                try {
                    return s3Service.uploadImage(image);
                } catch (InvalidImageException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }).toList();

            CelestialEvent newCelestialEventEntity = celestialEventDtoMapper.createDtoToCelestialEvent(celestialEvent);
            List<CelestialEventImage> celestialEventImages =
                    newCelestialEventEntity.convertImageToCelestialEventImage(imageUrls);
            newCelestialEventEntity.setImages(celestialEventImages);

            newCelestialEventEntity = celestialEventRepository.save(newCelestialEventEntity);

            GetCelestialEventDto createdEvent = celestialEventDtoMapper.celestialEventToGetDto(newCelestialEventEntity);
            return createdEvent;

        } catch (RuntimeException e) {
            throw new IncorrectCelestialEventFormatException();
        }

    }

    @CacheEvict(value = "singleCelestialEvent", key = "#celestialEventUuid")
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

    @CacheEvict(value = "singleCelestialEvent", key = "#celestialEventUuid")
    public GetCelestialEventDto updateCelestialEvent(String celestialEventUuid,
                                                     JsonPatch patch) {
        try {
            CelestialEvent celestialEvent =
                    celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
            CelestialEvent updatedCelestialEvent = applyPatchToCelestialEvent(patch, celestialEvent);
            celestialEventRepository.save(updatedCelestialEvent);

            GetCelestialEventDto eventDto =
                    celestialEventDtoMapper.celestialEventToGetDto(updatedCelestialEvent);

            return eventDto;
        } catch (JsonPatchException | JsonProcessingException exception) {
            return null;
        }
    }

    private CelestialEvent applyPatchToCelestialEvent(JsonPatch patch, CelestialEvent celestialEvent) throws JsonPatchException, JsonProcessingException {
        ObjectMapper mapper = jacksonConfig.objectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(celestialEvent, JsonNode.class));
        return mapper.treeToValue(patched, CelestialEvent.class);
    }


    @Cacheable(value = "celestialEventComment")
    public GetCelestialEventCommentDto addCommentToCelestialEvent(String celestialEventUuid,
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

        return returnDto;
    }

    @Cacheable(value = "celestialEventComment")
    public GetSlimCelestialEventCommentDto addReplyToCelestialEventComment(String celestialEventUuid,
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

        return returnDto;
    }
}




