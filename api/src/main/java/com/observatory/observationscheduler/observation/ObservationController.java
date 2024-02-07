package com.observatory.observationscheduler.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.startup.DataInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/users/{userUuid}/observations")
public class ObservationController {
    private final ObservationService service;

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getAllObservationsOfUser(@PathVariable String userUuid) {
        return service.getAllObservations(userUuid);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<Observation>> createObservation(@PathVariable String userUuid, @RequestPart("newObservation") Observation newObservation, @RequestPart("observationImage") MultipartFile[] image) {
        return service.createObservation(newObservation, userUuid, image);
    }

    @GetMapping("/{observationUuid}")
    public ResponseEntity<EntityModel<Observation>> getObservationByUuid(@PathVariable String observationUuid, @PathVariable String userUuid) {
        return service.getObservationByUuid(observationUuid, userUuid);
    }

    @PatchMapping(path = "/{observationUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<Observation>> patchObservation(@PathVariable String observationUuid, @RequestBody JsonPatch patch, @PathVariable String userUuid) {
        return service.patchObservation(observationUuid, patch);
    }

    @DeleteMapping("/{observationUuid}")
    public ResponseEntity<?> deleteObservation(@PathVariable String userUuid, @PathVariable String observationUuid) {
        return service.deleteObservation(userUuid, observationUuid);
    }
}
