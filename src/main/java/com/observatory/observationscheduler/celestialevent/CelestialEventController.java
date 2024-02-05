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
            return celestialEventService.getCelestialEventsByStatus(status.get());
        } else {
            return celestialEventService.getAllCelestialEvents();
        }
    }

    @GetMapping("/v1/celestial-events/{celestialEventUuid}")
    public EntityModel<CelestialEvent> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        return this.celestialEventService.getCelestialEventByUuid(celestialEventUuid);
    }

    @PatchMapping("/v1/celestial-events/batch-status")
    public CollectionModel<EntityModel<CelestialEvent>> updateCelestialEventStatus() {
        return celestialEventService.updateCelestialEventStatus();
    }

    @PostMapping("/v1/celestial-events")
    public EntityModel<CelestialEvent> createCelestialEvent(@RequestBody CelestialEvent celestialEvent){
        return celestialEventService.createCelestialEvent(celestialEvent);
    }


}
