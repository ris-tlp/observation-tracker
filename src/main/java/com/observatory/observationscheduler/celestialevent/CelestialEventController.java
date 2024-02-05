package com.observatory.observationscheduler.celestialevent;

import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CelestialEventController {
    public final CelestialEventService celestialEventService;

    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping("/v1/celestial-events")
    public CollectionModel<EntityModel<CelestialEvent>> getCelestialEventsByStatus(@RequestParam(required = false) Optional<CelestialEventStatus> status) {
        if (status.isPresent()) {
            return celestialEventService.getCelestialEventsByStatus(status);
        } else {
            return celestialEventService.getAllCelestialEvents();
        }
    }

    @PatchMapping("/v1/celestial-events/batch-status")
    public CollectionModel<EntityModel<CelestialEvent>> updateCelestialEventStatus() {
        return celestialEventService.updateCelestialEventStatus();
    }


}