package com.observatory.observationtracker.domain.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.observation.dto.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/observations")
public class ObservationController {
    private final ObservationService service;

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping(params = "userUuid")
    public ResponseEntity<CollectionModel<EntityModel<GetSlimObservationDto>>> getAllObservationsOfUser(@RequestParam String userUuid) {
        return service.getAllObservations(userUuid);
    }

    @PostMapping(params = {"userUuid", "celestialEventUuid"},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<GetObservationDto>> createObservation(
            @RequestParam String userUuid,
            @RequestParam String celestialEventUuid,
            @RequestPart(value = "newObservation") CreateObservationDto newObservation,
            @RequestPart(value = "images") List<MultipartFile> images
    ) {
        return service.createObservation(newObservation, userUuid, celestialEventUuid, images);
    }

    @GetMapping("/{observationUuid}")
    public ResponseEntity<EntityModel<GetObservationDto>> getObservationByUuid(@PathVariable String observationUuid) {
        return service.getObservationByUuid(observationUuid);
    }

    @PatchMapping(path = "/{observationUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetObservationDto>> patchObservation(@PathVariable String observationUuid,
                                                                           @RequestBody JsonPatch patch) {
        return service.patchObservation(observationUuid, patch);
    }

    @DeleteMapping("/{observationUuid}")
    public ResponseEntity<Void> deleteObservation(@PathVariable String observationUuid) {
        return service.deleteObservation(observationUuid);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<GetSlimObservationDto>>> getPublishedObservations() {
        return service.getPublishedCourses();
    }

    @PostMapping(value = "/{observationUuid}/comments", params = "userUuid", consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetObservationCommentDto> addCommentToObservation(@PathVariable String observationUuid, @RequestParam String userUuid,
                                                                            @RequestBody CreateObservationCommentDto newComment) {

        System.out.println("in here");
        return service.addCommentToObservation(observationUuid, userUuid, newComment);
    }

@PostMapping(value = "/{observationUuid}/comments", params = {"userUuid", "parentCommentUuid"}, consumes =
        MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetObservationCommentDto> addReplyToObservation(
            @PathVariable String observationUuid,
            @RequestParam String userUuid,
            @RequestParam String parentCommentUuid,
            @RequestBody CreateObservationCommentDto newReply
    )
    {
        return service.addReplyToObservation(observationUuid, userUuid, parentCommentUuid, newReply);
    }
}
