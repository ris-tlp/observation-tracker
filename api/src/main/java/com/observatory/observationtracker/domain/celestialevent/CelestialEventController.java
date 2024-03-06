package com.observatory.observationtracker.domain.celestialevent;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.celestialevent.assemblers.CelestialEventDtoAssembler;
import com.observatory.observationtracker.domain.celestialevent.assemblers.CelestialEventSlimDtoAssembler;
import com.observatory.observationtracker.domain.celestialevent.dto.*;
import com.observatory.observationtracker.domain.celestialevent.exceptions.IncorrectCelestialEventFormatException;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// @TODO: make superclass entity and everything
// @TODO: slim celestial event for batch searches
@RestController
@RequestMapping("/v1/celestial-events")
public class CelestialEventController {
    public final CelestialEventService celestialEventService;
    private final CelestialEventDtoAssembler dtoAssembler;
    private final CelestialEventSlimDtoAssembler slimDtoAssembler;
    private final PagedResourcesAssembler<GetSlimCelestialEventDto> pagedResourcesAssembler;


    public CelestialEventController(CelestialEventService celestialEventService,
                                    CelestialEventDtoAssembler dtoAssembler,
                                    CelestialEventSlimDtoAssembler slimDtoAssembler,
                                    PagedResourcesAssembler<GetSlimCelestialEventDto> pagedResourcesAssembler) {
        this.celestialEventService = celestialEventService;
        this.dtoAssembler = dtoAssembler;
        this.slimDtoAssembler = slimDtoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<GetSlimCelestialEventDto>>> getCelestialEvents(Pageable pageable) {
        return celestialEventService.getAllCelestialEvents(pageable);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<GetCelestialEventDto>> createCelestialEvent(@RequestPart(value =
            "newCelestialEvent") CreateCelestialEventDto newCelestialEvent, @RequestPart("images") List<MultipartFile> images) {
        GetCelestialEventDto eventDto = celestialEventService.createCelestialEvent(newCelestialEvent, images);
        Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoAssembler.toModel(eventDto).add(rootLink));
    }

    @GetMapping(params = "status")
    public ResponseEntity<CollectionModel<EntityModel<GetSlimCelestialEventDto>>> getCelestialEventsByStatus(
            Pageable pageable,
            @RequestParam CelestialEventStatus status
    ) {
        return celestialEventService.getCelestialEventsByStatus(status, pageable);
    }

    @GetMapping("/{celestialEventUuid}")
    public ResponseEntity<EntityModel<GetCelestialEventDto>> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        GetCelestialEventDto eventDto = celestialEventService.getCelestialEventByUuid(celestialEventUuid);
        Link rootLink = linkTo(CelestialEventController.class).withRel("all").withType("GET, POST");

        return ResponseEntity.status(HttpStatus.OK).body(dtoAssembler.toModel(eventDto).add(rootLink));
    }

    @PatchMapping(path = "/{celestialEventUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetCelestialEventDto>> updateCelestialEvent(@PathVariable String celestialEventUuid,
                                                                                  @RequestBody JsonPatch patch) {
        GetCelestialEventDto eventDto = celestialEventService.updateCelestialEvent(celestialEventUuid, patch);

        if (Objects.isNull(eventDto)) {
            throw new IncorrectCelestialEventFormatException();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(dtoAssembler.toModel(eventDto));

        }
    }

    @DeleteMapping("/{celestialEventUuid}")
    public ResponseEntity<Void> deleteCelestialEvent(@PathVariable String celestialEventUuid) {
        return celestialEventService.deleteCelestialEvent(celestialEventUuid);
    }

    @PatchMapping("/batch-status")
    public ResponseEntity<Void> updateCelestialEventStatus() {
        return celestialEventService.updateCelestialEventStatus();
    }

    @PostMapping(value = "/{celestialEventUuid}/comments", params = "userUuid", consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCelestialEventCommentDto> addCommentToCelestialEvent(@PathVariable String celestialEventUuid,
                                                                                  @RequestBody CreateCelestialEventCommentDto newComment,
                                                                                  @RequestParam String userUuid) {
        GetCelestialEventCommentDto commentDto = celestialEventService.addCommentToCelestialEvent(celestialEventUuid,
                userUuid, newComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @PostMapping(value = "/{celestialEventUuid}/comments", params = {"userUuid", "parentCommentUuid"}, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetSlimCelestialEventCommentDto> addReplyToCelestialEventComment(@PathVariable String celestialEventUuid,
                                                                                           @RequestBody CreateCelestialEventCommentDto newComment,
                                                                                           @RequestParam String userUuid,
                                                                                           @RequestParam String parentCommentUuid) {
        GetSlimCelestialEventCommentDto commentDto = celestialEventService.addReplyToCelestialEventComment(celestialEventUuid, userUuid, parentCommentUuid,
                newComment);

        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

}
