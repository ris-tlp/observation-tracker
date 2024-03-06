package com.observatory.observationtracker.domain.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.observation.assemblers.ObservationDtoAssembler;
import com.observatory.observationtracker.domain.observation.assemblers.ObservationSlimDtoAssembler;
import com.observatory.observationtracker.domain.observation.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/v1/observations")
public class ObservationController {
    private final ObservationService service;
    private final ObservationDtoAssembler observationDtoAssembler;
    private final ObservationSlimDtoAssembler observationSlimDtoAssembler;
    private final PagedResourcesAssembler<GetSlimObservationDto> pagedResourcesAssembler;


    public ObservationController(ObservationService service,
                                 ObservationDtoAssembler observationDtoAssembler,
                                 ObservationSlimDtoAssembler observationSlimDtoAssembler,
                                 PagedResourcesAssembler<GetSlimObservationDto> pagedResourcesAssembler
    ) {
        this.service = service;
        this.observationDtoAssembler = observationDtoAssembler;
        this.observationSlimDtoAssembler = observationSlimDtoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(params = "userUuid")
    public ResponseEntity<PagedModel<EntityModel<GetSlimObservationDto>>> getAllObservationsOfUser(@RequestParam String userUuid, Pageable pageable) {
        Page<GetSlimObservationDto> observationDtos = service.getAllObservations(userUuid, pageable);

        PagedModel<EntityModel<GetSlimObservationDto>> pagedObservations =
                pagedResourcesAssembler.toModel(observationDtos, observationSlimDtoAssembler);

        return ResponseEntity.status(HttpStatus.OK).body(
                pagedObservations
        );
    }

    @PostMapping(params = {"userUuid", "celestialEventUuid"},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<GetObservationDto>> createObservation(
            @RequestParam String userUuid,
            @RequestParam String celestialEventUuid,
            @RequestPart(value = "newObservation") CreateObservationDto newObservation,
            @RequestPart(value = "images") List<MultipartFile> images
    ) {
        GetObservationDto observationDto = service.createObservation(
                newObservation, userUuid, celestialEventUuid, images);
        Link rootLink =
                linkTo(ObservationController.class, observationDto.getOwner().getUuid()).withRel("all").withType(
                        "GET, POST");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                observationDtoAssembler.toModel(observationDto).add(rootLink));
    }

    @GetMapping("/{observationUuid}")
    public ResponseEntity<EntityModel<GetObservationDto>> getObservationByUuid(@PathVariable String observationUuid) {
        GetObservationDto observationDto = service.getObservationByUuid(observationUuid);

        Link rootLink =
                linkTo(ObservationController.class, observationDto.getOwner().getUuid()).withRel("all").withType(
                        "GET, POST");

        return ResponseEntity.status(HttpStatus.OK).body(observationDtoAssembler.toModel(observationDto).add(rootLink));

    }

    @PatchMapping(path = "/{observationUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetObservationDto>> patchObservation(@PathVariable String observationUuid,
                                                                           @RequestBody JsonPatch patch) {
        GetObservationDto observationDto = service.patchObservation(observationUuid, patch);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                observationDtoAssembler.toModel(observationDto)
        );
    }

    @DeleteMapping("/{observationUuid}")
    public ResponseEntity<Void> deleteObservation(@PathVariable String observationUuid) {
        return service.deleteObservation(observationUuid);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GetSlimObservationDto>>> getPublishedObservations(Pageable pageable) {
        Page<GetSlimObservationDto> observationsDto = service.getPublishedCourses(pageable);

        PagedModel<EntityModel<GetSlimObservationDto>> pagedObservations =
                pagedResourcesAssembler.toModel(observationsDto, observationSlimDtoAssembler);

        return ResponseEntity.status(HttpStatus.OK).body(pagedObservations);
    }

    @PostMapping(value = "/{observationUuid}/comments", params = "userUuid", consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetObservationCommentDto> addCommentToObservation(@PathVariable String observationUuid,
                                                                            @RequestParam String userUuid,
                                                                            @RequestBody CreateObservationCommentDto newComment) {

        GetObservationCommentDto commentDto = service.addCommentToObservation(observationUuid, userUuid, newComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @PostMapping(value = "/{observationUuid}/comments", params = {"userUuid", "parentCommentUuid"}, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetObservationCommentDto> addReplyToObservation(
            @PathVariable String observationUuid,
            @RequestParam String userUuid,
            @RequestParam String parentCommentUuid,
            @RequestBody CreateObservationCommentDto newReply
    ) {
        GetObservationCommentDto commentDto = service.addReplyToObservation(observationUuid, userUuid,
                parentCommentUuid, newReply);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }
}
