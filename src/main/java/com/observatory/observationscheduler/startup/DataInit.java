package com.observatory.observationscheduler.startup;

import com.observatory.observationscheduler.useraccount.UserAccount;
import com.observatory.observationscheduler.useraccount.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner{
    private static final Logger log = LoggerFactory.getLogger(DataInit.class);
    private final UserAccountRepository userRepo;

    public DataInit(UserAccountRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        userRepo.deleteAll();
        userRepo.save(new UserAccount("name", "emaila"));
        userRepo.save(new UserAccount("newName", "newEmail"));
    }
}
