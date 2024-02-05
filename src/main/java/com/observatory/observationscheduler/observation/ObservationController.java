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
public class ObservationController {
    public final ObservationService service;
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping("/v1/users/{userUuid}/observations")
    public CollectionModel<EntityModel<Observation>> getAllObservationsOfUser(@PathVariable String userUuid) {
        return service.getAllObservations(userUuid);
    }

    @GetMapping("/v1/users/{userUuid}/observations/{observationUuid}")
    public EntityModel<Observation> getObservationByUuid(@PathVariable String observationUuid, @PathVariable String userUuid) {
        return service.getObservationByUuid(observationUuid, userUuid);
    }

    @PatchMapping(path = "/v1/users/{userUuid}/observations/{observationUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<Observation>> patchObservation(@PathVariable String observationUuid, @RequestBody JsonPatch patch, @PathVariable String userUuid) {
        return service.patchObservation(observationUuid, patch);
    }

    @PostMapping(path = "/v1/users/{userUuid}/observations", consumes = "application/json")
    public EntityModel<Observation> createObservation(@PathVariable String userUuid, @RequestBody Observation newObservation) {
        return service.createObservation(newObservation, userUuid);
    }

    @DeleteMapping("/v1/users/{userUuid}/observations/{observationUuid}")
    public ResponseEntity<?> deleteObservation(@PathVariable String userUuid, @PathVariable String observationUuid) {
        return service.deleteObservation(userUuid, observationUuid);
    }
}
