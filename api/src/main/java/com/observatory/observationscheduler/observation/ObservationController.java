package com.observatory.observationscheduler.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.observation.dto.CreateObservationDto;
import com.observatory.observationscheduler.observation.dto.GetObservationDto;
import com.observatory.observationscheduler.observation.models.Observation;
import jakarta.websocket.server.PathParam;
import org.h2.util.json.JSONObject;
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
    public ResponseEntity<CollectionModel<EntityModel<GetObservationDto>>> getAllObservationsOfUser(@RequestParam String userUuid) {
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
    public ResponseEntity<CollectionModel<EntityModel<GetObservationDto>>> getPublishedObservations() {
        return service.getPublishedCourses();
    }
}
