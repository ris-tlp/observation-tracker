package com.observatory.observationscheduler.domain.celestialevent.repositories;

import com.observatory.observationscheduler.domain.celestialevent.models.CelestialEventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelestialEventImageRepository extends JpaRepository<CelestialEventImage, Long> {
}
