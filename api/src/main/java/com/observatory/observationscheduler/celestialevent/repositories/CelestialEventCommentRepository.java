package com.observatory.observationscheduler.celestialevent.repositories;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CelestialEventCommentRepository extends JpaRepository<CelestialEventComment, Long>{
    Optional<CelestialEventComment> findCelestialEventCommentByUuid(String uuid);
    List<CelestialEventComment> findByParentCommentIsNull();
}
