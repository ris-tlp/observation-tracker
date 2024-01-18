package com.observatory.observationscheduler.useraccount;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserAccountController {
    private UserAccountService service;

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

}
