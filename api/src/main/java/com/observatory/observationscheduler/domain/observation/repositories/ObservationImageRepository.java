package com.observatory.observationscheduler.domain.observation.repositories;

import com.observatory.observationscheduler.domain.observation.models.ObservationImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationImageRepository extends JpaRepository<ObservationImage, Long> {
}
