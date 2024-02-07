package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.observation.models.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationImageRepository extends JpaRepository<Observation, Long> {
}
