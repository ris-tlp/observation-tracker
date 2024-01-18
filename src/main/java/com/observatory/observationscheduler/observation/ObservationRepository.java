package com.observatory.observationscheduler.observation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    Optional<Observation> findObservationByUuid(String uuid);
}
