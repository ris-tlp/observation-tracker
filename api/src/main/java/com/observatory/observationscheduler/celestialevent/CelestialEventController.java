package com.observatory.observationscheduler.celestialevent;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.celestialevent.dto.*;
import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// @TODO: make superclass entity and everything
// @TODO: slim celestial event for batch searches
@RestController
@RequestMapping("/v1/celestial-events")
public class CelestialEventController {
    public final CelestialEventService celestialEventService;

    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<GetSlimCelestialEventDto>>> getCelestialEvents() {
        return celestialEventService.getAllCelestialEvents();
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<GetCelestialEventDto>> createCelestialEvent(@RequestPart(value =
            "newCelestialEvent") CreateCelestialEventDto newCelestialEvent, @RequestPart("images") List<MultipartFile> images) {
        return celestialEventService.createCelestialEvent(newCelestialEvent, images);
    }

    @GetMapping(params = "status")
    public ResponseEntity<CollectionModel<EntityModel<GetCelestialEventDto>>> getCelestialEventsByStatus(@RequestParam CelestialEventStatus status) {
        return celestialEventService.getCelestialEventsByStatus(status);
    }

    @GetMapping("/{celestialEventUuid}")
    public ResponseEntity<EntityModel<GetCelestialEventDto>> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        return celestialEventService.getCelestialEventByUuid(celestialEventUuid);
    }

    @PatchMapping(path = "/{celestialEventUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetCelestialEventDto>> updateCelestialEvent(@PathVariable String celestialEventUuid,
                                                                                  @RequestBody JsonPatch patch) {
        return celestialEventService.updateCelestialEvent(celestialEventUuid, patch);
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
        return celestialEventService.addCommentToCelestialEvent(celestialEventUuid, userUuid, newComment);
    }

    @PostMapping(value = "/{celestialEventUuid}/comments", params = {"userUuid", "parentCommentUuid"}, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetSlimCelestialEventCommentDto> addReplyToCelestialEventComment(@PathVariable String celestialEventUuid,
                                                                                    @RequestBody CreateCelestialEventCommentDto newComment, @RequestParam String userUuid, @RequestParam String parentCommentUuid) {
        return celestialEventService.addReplyToCelestialEventComment(celestialEventUuid, userUuid, parentCommentUuid,
                newComment);
    }

}
