package com.observatory.observationscheduler.celestialevent;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<CelestialEvent>> createCelestialEvent(@RequestPart(value = "newCelestialEvent") CelestialEvent newCelestialEvent, @RequestPart("images") List<MultipartFile> images) {
        return celestialEventService.createCelestialEvent(newCelestialEvent, images);
    }

    @GetMapping(params = "status")
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEventsByStatus(@RequestParam CelestialEventStatus status) {
        return celestialEventService.getCelestialEventsByStatus(status);
    }

    @GetMapping("/{celestialEventUuid}")
    public ResponseEntity<EntityModel<CelestialEvent>> getCelestialEventByUuid(@PathVariable String celestialEventUuid) {
        return celestialEventService.getCelestialEventByUuid(celestialEventUuid);
    }

    @PatchMapping(path = "/{celestialEventUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<CelestialEvent>> updateCelestialEvent(@PathVariable String celestialEventUuid,
                                                                            @RequestBody JsonPatch patch) {
        return celestialEventService.updateCelestialEvent(celestialEventUuid, patch);
    }

    @DeleteMapping("/{celestialEventUuid}")
    public ResponseEntity<Void> deleteCelestialEvent(@PathVariable String celestialEventUuid) {
        return celestialEventService.deleteCelestialEvent(celestialEventUuid);
    }

    @PatchMapping("/batch-status")
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> updateCelestialEventStatus() {
        return celestialEventService.updateCelestialEventStatus();
    }
}
