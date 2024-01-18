package com.observatory.observationscheduler.observation;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ObservationController {
    public final ObservationService service;

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping("/observations")
    public CollectionModel<EntityModel<Observation>> getAllObservations() {
        return service.getAllObservations();
    }

    @GetMapping("/observations/{uuid}")
    public EntityModel<Observation> getObservationByUuid(@PathVariable String uuid) {
        return service.getObservationByUuid(uuid);
    }
}
