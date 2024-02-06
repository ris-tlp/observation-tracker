package com.observatory.observationscheduler.observation;

import com.observatory.observationscheduler.useraccount.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    Optional<Observation> findObservationByUuid(String uuid);
    Optional<List<Observation>> findByOwnerUuid(String uuid);
}
