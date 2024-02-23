package com.observatory.observationtracker.domain.observation.repositories;

import com.observatory.observationtracker.domain.observation.models.ObservationImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationImageRepository extends JpaRepository<ObservationImage, Long> {
}
