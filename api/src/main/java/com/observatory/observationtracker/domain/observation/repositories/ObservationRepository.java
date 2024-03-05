package com.observatory.observationtracker.domain.observation.repositories;

import com.observatory.observationtracker.domain.observation.models.Observation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    Optional<Observation> findObservationByUuid(String uuid);
    Page<Observation> findObservationsByIsPublishedIsTrue(Pageable pageable);


    Page<Observation> findByOwnerUuid(String uuid, Pageable pageable);

    @Query("SELECT o FROM Observation o LEFT JOIN FETCH o.comments com WHERE o.uuid = :uuid AND com.parentComment " +
            "IS NULL")
    Optional<Observation> findByNullParentComment(@Param("uuid") String uuid);
}
