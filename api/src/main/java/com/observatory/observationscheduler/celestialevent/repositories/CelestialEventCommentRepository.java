package com.observatory.observationscheduler.celestialevent.repositories;

import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CelestialEventCommentRepository extends JpaRepository<CelestialEventComment, Long> {
//    List<CelestialEventComment> findByCelestialEventUuid(String celestialEventUuid);
    Optional<CelestialEventComment> findCelestialEventCommentByUuid(String uuid);
//    List<CelestialEventComment> findByParentCommentUuid(String parentCommentUuid);
//    List<CelestialEventComment> findByCelestialEventUuidAndParentCommentIsNull(String celestialEventUuid);
}
