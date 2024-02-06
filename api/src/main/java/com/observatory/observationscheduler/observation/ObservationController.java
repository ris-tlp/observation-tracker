package com.observatory.observationscheduler.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.startup.DataInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<Observation>> createObservation(@PathVariable String userUuid, @RequestBody Observation newObservation) {
        return service.createObservation(newObservation, userUuid);
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
