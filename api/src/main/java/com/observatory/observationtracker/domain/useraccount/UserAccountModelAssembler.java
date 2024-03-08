package com.observatory.observationtracker.domain.useraccount;

import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAccountModelAssembler implements RepresentationModelAssembler<GetUserAccountDto,
        EntityModel<GetUserAccountDto>> {
    @Override
    public EntityModel<GetUserAccountDto> toModel(GetUserAccountDto account) {
        return EntityModel.of(account,
                linkTo(methodOn(UserAccountController.class).getOneUserByUuid(account.getUuid())).withSelfRel().withType("GET, PATCH"), linkTo(methodOn(UserAccountController.class).createUser(null)).withRel("user").withType("POST"));
    }

    @Override
    public CollectionModel<EntityModel<GetUserAccountDto>> toCollectionModel(Iterable<? extends GetUserAccountDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
