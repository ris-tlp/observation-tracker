package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.observation.models.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    Optional<Observation> findObservationByUuid(String uuid);
    Optional<List<Observation>> findObservationsByIsPublishedIsTrue();
    Optional<List<Observation>> findByOwnerUuid(String uuid);

    @Query("SELECT o FROM Observation o LEFT JOIN FETCH o.comments com WHERE o.uuid = :uuid AND com.parentComment " +
            "IS NULL")
    Optional<Observation> findByNullParentComment(@Param("uuid") String uuid);
}
