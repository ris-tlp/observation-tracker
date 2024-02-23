package com.observatory.observationscheduler.domain.celestialevent.repositories;

import com.observatory.observationscheduler.domain.celestialevent.models.CelestialEventComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CelestialEventCommentRepository extends JpaRepository<CelestialEventComment, Long>{
    Optional<CelestialEventComment> findCelestialEventCommentByUuid(String uuid);
    List<CelestialEventComment> findByParentCommentIsNull();

    List<CelestialEventComment> findByParentCommentIsNullAndCelestialEvent_Uuid(String uuid);

}
