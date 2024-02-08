package com.observatory.observationscheduler.celestialevent.repositories;

import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CelestialEventImageRepository extends JpaRepository<CelestialEventImage, Long> {
    Optional<CelestialEventImage> findCelestialEventImageByUuid(String uuid);
}
