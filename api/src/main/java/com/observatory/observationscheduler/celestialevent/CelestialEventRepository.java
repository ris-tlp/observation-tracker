package com.observatory.observationscheduler.celestialevent;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CelestialEventRepository extends JpaRepository<CelestialEvent, Long> {
    Optional<List<CelestialEvent>> findCelestialEventByEventStatus(CelestialEventStatus status);
    Optional<CelestialEvent> findCelestialEventByUuid(String uuid);
}
