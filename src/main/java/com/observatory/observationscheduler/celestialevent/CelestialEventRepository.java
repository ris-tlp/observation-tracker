package com.observatory.observationscheduler.celestialevent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CelestialEventRepository extends JpaRepository<CelestialEvent, Long> {
    Optional<List<CelestialEvent>> getCelestialEventByEventStatus(CelestialEventStatus status);
}