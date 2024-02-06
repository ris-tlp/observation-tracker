package com.observatory.observationscheduler.startup;

import com.observatory.observationscheduler.celestialevent.CelestialEvent;
import com.observatory.observationscheduler.celestialevent.CelestialEventRepository;
import com.observatory.observationscheduler.observation.Observation;
import com.observatory.observationscheduler.observation.ObservationRepository;
import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataInit implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);
    private final UserAccountRepository userRepo;
    private final ObservationRepository observationRepository;
    private final CelestialEventRepository eventRepository;

    public DataInit(UserAccountRepository userRepo, ObservationRepository observationRepository, CelestialEventRepository eventRepository) {
        this.observationRepository = observationRepository;
        this.userRepo = userRepo;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        observationRepository.deleteAll();
//        userRepo.deleteAll();
//        eventRepository.deleteAll();

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
//



    }
}
