package com.observatory.observationscheduler.startup;

import com.observatory.observationscheduler.celestialevent.repositories.CelestialEventRepository;
import com.observatory.observationscheduler.observation.repositories.ObservationImageRepository;
import com.observatory.observationscheduler.observation.repositories.ObservationRepository;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);
    private final UserAccountRepository userRepo;
    private final ObservationRepository observationRepository;
    private final CelestialEventRepository eventRepository;
    private final ObservationImageRepository imageRepo;

    public DataInit(UserAccountRepository userRepo, ObservationRepository observationRepository, CelestialEventRepository eventRepository, ObservationImageRepository imageRepo) {
        this.observationRepository = observationRepository;
        this.userRepo = userRepo;
        this.eventRepository = eventRepository;
        this.imageRepo = imageRepo;
    }

    @Override
    public void run(String... args) throws Exception {
//        observationRepository.deleteAll();
//        imageRepo.deleteAll();
//        userRepo.deleteAll();
//        eventRepository.deleteAll();
//
//        userRepo.saveAndFlush(new UserAccount("name", "emaila"));
//        UserAccount user = new UserAccount("newName", "newEmail");
//        userRepo.saveAndFlush(user);
//
//        observationRepository.saveAndFlush(new Observation("new observation","new description", user));
//        observationRepository.saveAndFlush(new Observation("new new","ultra new" , user));
//
//
//        eventRepository.saveAndFlush(new CelestialEvent("Event name", "Event Description", LocalDateTime.of(LocalDate.of(1999, 11, 11), LocalTime.now())));
//        eventRepository.saveAndFlush(new CelestialEvent("Event name numbah 2", "Event Description numbah 2", LocalDateTime.of(LocalDate.of(2055, 11, 11), LocalTime.now())));
//        eventRepository.saveAndFlush(new CelestialEvent("PAST Event name numbah 2", "Event Description numbah 2", LocalDateTime.of(LocalDate.of(2076, 11, 11), LocalTime.now())));




    }
}
