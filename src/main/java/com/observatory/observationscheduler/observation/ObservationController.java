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

    @GetMapping("/v1/users/{user_uuid}/observations")
    public CollectionModel<EntityModel<Observation>> getAllObservations() {
        return service.getAllObservations();
    }

    @GetMapping("/v1/users/{user_uuid}/observations/{observation_uuid}")
    public EntityModel<Observation> getObservationByUuid(@PathVariable String observation_uuid) {
        return service.getObservationByUuid(observation_uuid);
    }

    @PatchMapping(path = "/v1/users/{user_uuid}/observations/{observation_uuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<Observation>> patchObservation(@PathVariable String observation_uuid, @RequestBody JsonPatch patch) {
        return service.patchObservation(observation_uuid, patch);
    }

    @PostMapping(path = "/v1/users/{user_uuid}/observations", consumes = "application/json")
    public EntityModel<Observation> createObservation(@PathVariable String user_uuid, @RequestBody Observation newObservation) {
        return service.createObservation(newObservation, user_uuid);
    }

}
