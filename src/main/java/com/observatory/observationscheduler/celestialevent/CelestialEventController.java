package com.observatory.observationscheduler.celestialevent;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CelestialEventController {
    public final CelestialEventService celestialEventService;

    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping("/v1/celestial-events")
    public CollectionModel<EntityModel<CelestialEvent>> getAllCelestialEvents() {
        return celestialEventService.getAllCelestialEvents();
    }


}
