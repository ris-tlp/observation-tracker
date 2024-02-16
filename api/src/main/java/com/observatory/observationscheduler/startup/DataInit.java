package com.observatory.observationscheduler.startup;

import com.observatory.observationscheduler.celestialevent.models.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.models.CelestialEventImage;
import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationscheduler.observation.models.Observation;
import com.observatory.observationscheduler.observation.repositories.ObservationImageRepository;
import com.observatory.observationscheduler.observation.repositories.ObservationRepository;
import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

//        userRepo.saveAndFlush(new UserAccount("name", "emaila"));
//        UserAccount user = new UserAccount("newName", "newEmail");
//        userRepo.saveAndFlush(user);
//
////
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
