package com.observatory.observationtracker.startup;

import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationtracker.domain.observation.models.Observation;
import com.observatory.observationtracker.domain.observation.repositories.ObservationImageRepository;
import com.observatory.observationtracker.domain.observation.repositories.ObservationRepository;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import com.observatory.observationtracker.domain.useraccount.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInit implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);
    private final UserAccountRepository userRepo;
    private final ObservationRepository observationRepository;
    private final CelestialEventRepository eventRepository;
    private final ObservationImageRepository imageRepo;

    public DataInit(UserAccountRepository userRepo, ObservationRepository observationRepository,
                    CelestialEventRepository eventRepository, ObservationImageRepository imageRepo) {
        this.observationRepository = observationRepository;
        this.userRepo = userRepo;
        this.eventRepository = eventRepository;
        this.imageRepo = imageRepo;
    }

    @Override
    public void run(String... args) throws Exception {
//        imageRepo.deleteAll();
//        observationRepository.deleteAll();
//        userRepo.deleteAll();
//        eventRepository.deleteAll();


//        UserAccount user = new UserAccount("newName", "newEmail");
//        UserAccount user1 = new UserAccount("Name", "Emaila");
//        userRepo.saveAll(Arrays.asList(user, user1));
//
//        CelestialEvent event = new CelestialEvent("Event name", "Event Description", LocalDateTime.of
//                (LocalDate.of(1999, 11, 11), LocalTime.now()));
//        eventRepository.saveAndFlush(event);
//        eventRepository.saveAndFlush(new CelestialEvent("Event name numbah 2", "Event Description numbah 2",
//                LocalDateTime.of(LocalDate.of(2055, 11, 11), LocalTime.now())));
//        eventRepository.saveAndFlush(new CelestialEvent("PAST Event name numbah 2", "Event Description numbah 2",
//                LocalDateTime.of(LocalDate.of(2076, 11, 11), LocalTime.now())));
//
//        observationRepository.save(
//                new Observation(
//                        "new observation",
//                        "new description",
//                        user,
//                        LocalDateTime.of(LocalDate.of(2055, 11, 11), LocalTime.now()),
//                        event
//                )
//        );
//
//        observationRepository.saveAndFlush(
//                new Observation(
//                        "new new",
//                        "ultra new",
//                        user,
//                        LocalDateTime.of(LocalDate.of(2055, 11, 11), LocalTime.now()),
//                        event
//                )
//        );


    }
}
