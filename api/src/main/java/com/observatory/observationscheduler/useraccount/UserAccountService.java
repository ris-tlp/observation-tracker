package com.observatory.observationscheduler.useraccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationscheduler.useraccount.exceptions.UserNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserAccountService {
    private final UserAccountRepository repository;
    private final UserModelAssembler assembler;

    public UserAccountService(UserAccountRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    public EntityModel<UserAccount> oneUserByUuid(String uuid) {
        UserAccount account = repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        return assembler.toModel(account);
    }

    public ResponseEntity<EntityModel<UserAccount>> createUser(UserAccount newAccount) {
        EntityModel<UserAccount> entityModel = assembler.toModel(repository.save(newAccount));

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    public ResponseEntity<EntityModel<UserAccount>> patchUser(String uuid, JsonPatch patch) {
        try {
            UserAccount user =
                    repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
            UserAccount userPatched = applyPatchToUser(patch, user);
            repository.save(userPatched);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(userPatched));
        } catch (JsonPatchException | JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private UserAccount applyPatchToUser(JsonPatch patch, UserAccount user) throws JsonPatchException,
            JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(user, JsonNode.class));
        return mapper.treeToValue(patched, UserAccount.class);
    }

    public ResponseEntity<CollectionModel<EntityModel<UserAccount>>> getAllUsers() {
        List<UserAccount> allUserAccounts = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toCollectionModel(allUserAccounts));
    }
}


@Component
class UserModelAssembler implements RepresentationModelAssembler<UserAccount, EntityModel<UserAccount>> {
    @Override
    public EntityModel<UserAccount> toModel(UserAccount account) {
        return EntityModel.of(account,
                linkTo(methodOn(UserAccountController.class).getOneUserByUuid(account.getUuid())).withSelfRel().withType("GET, PATCH"), linkTo(methodOn(UserAccountController.class).createUser(null)).withRel("user").withType("POST"));
    }

    @Override
    public CollectionModel<EntityModel<UserAccount>> toCollectionModel(Iterable<? extends UserAccount> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
