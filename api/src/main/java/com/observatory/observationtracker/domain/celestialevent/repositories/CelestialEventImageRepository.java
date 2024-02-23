package com.observatory.observationtracker.domain.celestialevent.repositories;

import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelestialEventImageRepository extends JpaRepository<CelestialEventImage, Long> {
}
