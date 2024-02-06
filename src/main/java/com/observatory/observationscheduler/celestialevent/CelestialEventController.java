package com.observatory.observationscheduler.celestialevent;

import com.github.fge.jsonpatch.JsonPatch;
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

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<CelestialEvent>> createCelestialEvent(@RequestBody CelestialEvent celestialEvent) {
        return celestialEventService.createCelestialEvent(celestialEvent);
    }

    @GetMapping(params = "status")
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEventsByStatus(@RequestParam CelestialEventStatus status) {
        return celestialEventService.getCelestialEventsByStatus(status);
    }

    @GetMapping("/{celestialEventUuid}")
    public ResponseEntity<EntityModel<CelestialEvent>> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        return celestialEventService.getCelestialEventByUuid(celestialEventUuid);
    }

    // @TODO: Figure out issue with dependency
    @PatchMapping(path = "/{celestialEventUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<CelestialEvent>> updateCelestialEvent(@PathVariable String celestialEventUuid, @RequestBody JsonPatch patch) {
        return celestialEventService.updateCelestialEvent(celestialEventUuid, patch);
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
