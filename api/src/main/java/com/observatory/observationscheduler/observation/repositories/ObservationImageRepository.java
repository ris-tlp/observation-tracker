package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.observation.models.ObservationImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationImageRepository extends JpaRepository<ObservationImage, Long> {
}
