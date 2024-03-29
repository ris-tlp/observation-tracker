package com.observatory.observationtracker.domain.observation.repositories;

import com.observatory.observationtracker.domain.observation.models.ObservationComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObservationCommentRepository extends JpaRepository<ObservationComment, Long> {
    Optional<ObservationComment> findObservationCommentByUuid(String uuid);
}
