package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.observation.models.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    Optional<Observation> findObservationByUuid(String uuid);
    Optional<List<Observation>> findObservationsByIsPublishedIsTrue();
    Optional<List<Observation>> findByOwnerUuid(String uuid);
}
