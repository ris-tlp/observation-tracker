package com.observatory.observationtracker.domain.useraccount;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/users")
public class UserAccountController {
    private final UserAccountService service;

    public UserAccountController(UserAccountService service) {
        this.service = service;
    }


    @GetMapping("/{uuid}")
    public EntityModel<GetUserAccountDto> getOneUserByUuid(@PathVariable String uuid) {
        return service.oneUserByUuid(uuid);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<GetUserAccountDto>> createUser(@RequestBody UserAccount newAccount) {
        return service.createUser(newAccount);
    }

    /*
     * Uses JSON Patch RFC 6902 for the patch format
     * */
    @PatchMapping(value = "/{uuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetUserAccountDto>> patchUser(@PathVariable String uuid,
                                                                    @RequestBody JsonPatch patch) {
        return service.patchUser(uuid, patch);
    }


    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GetUserAccountDto>>> getAllUsers(Pageable pageable) {
        return service.getAllUsers(pageable);
    }
}
