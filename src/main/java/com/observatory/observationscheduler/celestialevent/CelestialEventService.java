package com.observatory.observationscheduler.celestialevent;

import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventStatusNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.CelestialEventUuidNotFoundException;
import com.observatory.observationscheduler.celestialevent.exceptions.IncorrectCelestialEventFormatException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CelestialEventService {
    private final CelestialEventRepository celestialEventRepository;
    private final CelestialEventAssembler assembler;

    public CelestialEventService(CelestialEventRepository celestialEventRepository, CelestialEventAssembler assembler) {
        this.celestialEventRepository = celestialEventRepository;
        this.assembler = assembler;
    }

    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getAllCelestialEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toCollectionModel(celestialEventRepository.findAll()));
    }

    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> getCelestialEventsByStatus(CelestialEventStatus status) {
        List<CelestialEvent> events = celestialEventRepository.findCelestialEventByEventStatus(status).orElseThrow(() -> new CelestialEventStatusNotFoundException(status));
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toCollectionModel(events));
    }

    /*
     * Temporary way to batch update all celestial events that have already expired or completed
     */
    public ResponseEntity<CollectionModel<EntityModel<CelestialEvent>>> updateCelestialEventStatus() {
        List<CelestialEvent> events = celestialEventRepository.findCelestialEventByEventStatus(CelestialEventStatus.UPCOMING).orElseThrow(() -> new CelestialEventStatusNotFoundException(CelestialEventStatus.UPCOMING));
        List<CelestialEvent> updatedEvents = events.stream().map(this::updateEventStatus).filter(Objects::nonNull).toList();

        return ResponseEntity.status(HttpStatus.OK).body(assembler.toCollectionModel(updatedEvents));
    }

    /*
     * Checks the DateTime of each celestial event and see if it is older than the current time, and mark it as completed if so.
     */
    private CelestialEvent updateEventStatus(CelestialEvent celestialEvent) {
        if (celestialEvent.getCelestialEventDateTime().isBefore(LocalDateTime.now()) && celestialEvent.getEventStatus() == CelestialEventStatus.UPCOMING) {
            celestialEvent.setEventStatus(CelestialEventStatus.COMPLETED);
            celestialEventRepository.saveAndFlush(celestialEvent);
            return celestialEvent;
        }
        return null;
    }

    public ResponseEntity<EntityModel<CelestialEvent>> getCelestialEventByUuid(String celestialEventUuid) {
        CelestialEvent event = celestialEventRepository.findCelestialEventByUuid(celestialEventUuid).orElseThrow(() -> new CelestialEventUuidNotFoundException(celestialEventUuid));
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(event));
    }

    public ResponseEntity<EntityModel<CelestialEvent>> createCelestialEvent(CelestialEvent celestialEvent) {
        try {
            CelestialEvent createdEvent = celestialEventRepository.save(celestialEvent);
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(createdEvent));
        } catch (RuntimeException e) {
            throw new IncorrectCelestialEventFormatException();
        }
    }
}

@Component
class CelestialEventAssembler implements RepresentationModelAssembler<CelestialEvent, EntityModel<CelestialEvent>> {

    @Override
    public EntityModel<CelestialEvent> toModel(CelestialEvent celestialEvent) {
        return EntityModel.of(celestialEvent, linkTo(methodOn(CelestialEventController.class).getCelestialEventByUuid(celestialEvent.getUuid())).withSelfRel().withType("GET"), linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(Optional.empty())).withRel("get-celestial-events").withType("GET"), linkTo(methodOn(CelestialEventController.class).createCelestialEvent(celestialEvent)).withRel("create-celestial-events").withType("POST"));
    }

    @Override
    public CollectionModel<EntityModel<CelestialEvent>> toCollectionModel(Iterable<? extends CelestialEvent> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
