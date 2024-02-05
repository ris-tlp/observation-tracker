package com.observatory.observationscheduler.celestialevent;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
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

    public CollectionModel<EntityModel<CelestialEvent>> getAllCelestialEvents() {
        return assembler.toCollectionModel(celestialEventRepository.findAll());
    }

    public CollectionModel<EntityModel<CelestialEvent>> getCelestialEventsByStatus(Optional<CelestialEventStatus> status) {
        return assembler.toCollectionModel(celestialEventRepository.getCelestialEventByEventStatus(status.get()).orElseThrow());

    }

    /*
    * Temporary way to batch update all celestial events that have already expired or completed
    */
    public CollectionModel<EntityModel<CelestialEvent>> updateCelestialEventStatus() {
        List<CelestialEvent> events = celestialEventRepository.getCelestialEventByEventStatus(CelestialEventStatus.UPCOMING).orElseThrow();
        List<CelestialEvent> updatedEvents = events.stream().map(this::updateEventStatus).filter(Objects::nonNull).toList();

        return assembler.toCollectionModel(updatedEvents);
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
}

@Component
class CelestialEventAssembler implements RepresentationModelAssembler<CelestialEvent, EntityModel<CelestialEvent>> {

    @Override
    public EntityModel<CelestialEvent> toModel(CelestialEvent celestialEvent) {
        return EntityModel.of(
                celestialEvent,
                linkTo(methodOn(CelestialEventController.class).getCelestialEventsByStatus(Optional.ofNullable(celestialEvent.getEventStatus()))).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<CelestialEvent>> toCollectionModel(Iterable<? extends CelestialEvent> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
