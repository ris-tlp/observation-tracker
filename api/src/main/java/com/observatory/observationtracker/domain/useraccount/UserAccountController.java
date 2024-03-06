package com.observatory.observationtracker.domain.useraccount;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/v1/users")
public class UserAccountController {
    private final UserAccountService service;
    
    private final UserAccountModelAssembler assembler;
    private final PagedResourcesAssembler<GetUserAccountDto> pagedResourcesAssembler;


    public UserAccountController(UserAccountService service, UserAccountModelAssembler assembler,
                                 PagedResourcesAssembler<GetUserAccountDto> pagedResourcesAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<GetUserAccountDto>> getOneUserByUuid(@PathVariable String uuid) {
        GetUserAccountDto userDto = service.oneUserByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(userDto));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<GetUserAccountDto>> createUser(@RequestBody UserAccount newAccount) {
        GetUserAccountDto userDto = service.createUser(newAccount);
        EntityModel<GetUserAccountDto> entityModel = assembler.toModel(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    /*
     * Uses JSON Patch RFC 6902 for the patch format
     * */
    @PatchMapping(value = "/{uuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<GetUserAccountDto>> patchUser(@PathVariable String uuid,
                                                                    @RequestBody JsonPatch patch) {
        GetUserAccountDto userDto = service.patchUser(uuid, patch);

        if (Objects.isNull(userDto)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(userDto));
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GetUserAccountDto>>> getAllUsers(Pageable pageable) {
        Page<GetUserAccountDto> usersDto = service.getAllUsers(pageable);
        PagedModel<EntityModel<GetUserAccountDto>> pagedUsers = pagedResourcesAssembler.toModel(usersDto, assembler);

        return ResponseEntity.status(HttpStatus.OK).body(pagedUsers);
    }
}
