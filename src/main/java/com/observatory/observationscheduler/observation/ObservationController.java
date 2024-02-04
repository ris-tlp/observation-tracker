package com.observatory.observationscheduler.observation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.startup.DataInit;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ObservationController {
    public final ObservationService service;
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping("/v1/observations")
    public CollectionModel<EntityModel<Observation>> getAllObservations() {
        return service.getAllObservations();
    }

    @GetMapping("/v1/observations/{uuid}")
    public EntityModel<Observation> getObservationByUuid(@PathVariable String uuid) {
        return service.getObservationByUuid(uuid);
    }

    @PatchMapping(path = "/v1/observations/{uuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<Observation>> patchObservation(@PathVariable String uuid, @RequestBody JsonPatch patch) {
        return service.patchObservation(uuid, patch);
    }

    @PostMapping(path = "/v1/observations", consumes = "application/json")
    public EntityModel<Observation> createObservation(@RequestBody Observation newObservation) {
        return service.createObservation(newObservation);
    }

}
