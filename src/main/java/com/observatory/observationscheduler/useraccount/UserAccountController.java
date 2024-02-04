package com.observatory.observationscheduler.useraccount;

import com.github.fge.jsonpatch.JsonPatch;
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
    public CollectionModel<EntityModel<UserAccount>> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/users/{uuid}")
    public EntityModel<UserAccount> getOneUserByUuid(@PathVariable String uuid) {
        return service.oneUserByUuid(uuid);
    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<UserAccount>> createUser(@RequestBody UserAccount newAccount) {
        return service.createUser(newAccount);
    }

    /*
    * Uses JSON Patch RFC 6902 for the patch format
    * */
    @PatchMapping("/users/{uuid}")
    public ResponseEntity<EntityModel<UserAccount>> patchUser(@PathVariable String uuid, @RequestBody JsonPatch patch) {
        return service.patchUser(uuid, patch);
    }
}
