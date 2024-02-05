package com.observatory.observationscheduler.celestialevent;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CelestialEventController {
    public final CelestialEventService celestialEventService;

    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping("/v1/celestial-events")
    public CollectionModel<EntityModel<CelestialEvent>> getCelestialEventsByStatus(@RequestParam(required = false) CelestialEventStatus status) {
        if (status != null) {
            return celestialEventService.getCelestialEventsByStatus(status);
        } else {
            return celestialEventService.getAllCelestialEvents();
        }
    }


}
