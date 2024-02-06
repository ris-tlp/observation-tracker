package com.observatory.observationscheduler.celestialevent;

import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/celestial-events")
public class CelestialEventController {
    public final CelestialEventService celestialEventService;

    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEvents() {
        return celestialEventService.getAllCelestialEvents();
    }

    @PostMapping
    public ResponseEntity<EntityModel<CelestialEvent>> createCelestialEvent(@RequestBody CelestialEvent celestialEvent) {
        return celestialEventService.createCelestialEvent(celestialEvent);
    }

    @GetMapping(params = "status")
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEventsByStatus(@RequestParam CelestialEventStatus status) {
        return celestialEventService.getCelestialEventsByStatus(status);
    }

    @GetMapping("/{celestialEventUuid}")
    public ResponseEntity<EntityModel<CelestialEvent>> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        return this.celestialEventService.getCelestialEventByUuid(celestialEventUuid);
    }

    @DeleteMapping("/{celestialEventUuid}")
    public ResponseEntity<?> deleteCelestialEvent(@PathVariable String celestialEventUuid) {
        return celestialEventService.deleteCelestialEvent(celestialEventUuid);
    }

    @PatchMapping("/batch-status")
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> updateCelestialEventStatus() {
        return celestialEventService.updateCelestialEventStatus();
    }
}
