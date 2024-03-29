package com.observatory.observationtracker.domain.celestialevent.repositories;

import com.observatory.observationtracker.domain.celestialevent.CelestialEventController;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.celestialevent.models.CelestialEventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CelestialEventRepository extends JpaRepository<CelestialEvent, Long> {
    Optional<List<CelestialEvent>> findCelestialEventByEventStatus(CelestialEventStatus status);

    Optional<Page<CelestialEvent>> findPagedCelestialEventByEventStatus(CelestialEventStatus status, Pageable page);

    @Query("SELECT c FROM CelestialEvent c LEFT JOIN FETCH c.comments com WHERE c.uuid = :uuid AND com.parentComment " +
            "IS NULL")
    Optional<CelestialEvent> findByNullParentComment(@Param("uuid") String uuid);

    Optional<CelestialEvent> findCelestialEventByUuid(String uuid);
}
