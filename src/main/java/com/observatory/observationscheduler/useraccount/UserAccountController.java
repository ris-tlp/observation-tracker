package com.observatory.observationscheduler.useraccount;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserAccountController {
    private final UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<UserAccount>> allUsers() {
        return service.allUsers();
    }

    @GetMapping("/users/{id}")
    public EntityModel<UserAccount> oneUser(@PathVariable Long id) {
        return service.oneUser(id);
    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<UserAccount>> createUser(@RequestBody UserAccount newAccount) {
        return service.createUser(newAccount);
    }
    }
