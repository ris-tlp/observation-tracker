package com.observatory.observationscheduler.startup;

import com.observatory.observationscheduler.observation.Observation;
import com.observatory.observationscheduler.observation.ObservationRepository;
import com.observatory.observationscheduler.useraccount.UserAccount;
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

    public DataInit(UserAccountRepository userRepo, ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        observationRepository.deleteAll();
        userRepo.deleteAll();

        userRepo.save(new UserAccount("name", "emaila"));
        UserAccount user = new UserAccount("newName", "newEmail");
        userRepo.save(user);

        observationRepository.save(new Observation("new observation", user));
        observationRepository.save(new Observation("new new", user));


    }
}
