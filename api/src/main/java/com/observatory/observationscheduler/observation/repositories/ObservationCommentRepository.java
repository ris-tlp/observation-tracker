package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.observation.models.ObservationComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObservationCommentRepository extends JpaRepository<ObservationComment, Long> {
    Optional<ObservationComment> findObservationCommentByUuid(String uuid);
}
