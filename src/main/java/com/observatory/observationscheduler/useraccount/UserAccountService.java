package com.observatory.observationscheduler.useraccount;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

    public CollectionModel<EntityModel<UserAccount>> allUsers() {
        CollectionModel<EntityModel<UserAccount>> assembledRequest = assembler.toCollectionModel(repository.findAll());
        return CollectionModel.of(
                assembledRequest,
                linkTo(methodOn(UserAccountController.class).allUsers()).withSelfRel()
        );
    }

    public EntityModel<UserAccount> oneUser(Long id) {
        UserAccount account = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return assembler.toModel(account);
    }

    public ResponseEntity<EntityModel<UserAccount>> createUser(UserAccount newAccount) {
        EntityModel<UserAccount> entityModel = assembler.toModel(repository.save(newAccount));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entityModel);
    }
}


@Component
class UserModelAssembler implements RepresentationModelAssembler<UserAccount, EntityModel<UserAccount>> {
    @Override
    public EntityModel<UserAccount> toModel(UserAccount account) {
        return EntityModel.of(
                account,
                linkTo(methodOn(UserAccountController.class).oneUser(account.getUserId())).withSelfRel(),
                linkTo(methodOn(UserAccountController.class).allUsers()).withRel("user-accounts"));
    }

    @Override
    public CollectionModel<EntityModel<UserAccount>> toCollectionModel(Iterable<? extends UserAccount> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
