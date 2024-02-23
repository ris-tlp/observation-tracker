package com.observatory.observationscheduler.observation.repositories;

import com.observatory.observationscheduler.observation.models.ObservationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationCommentRepository extends JpaRepository<ObservationComment, Long> {
}
