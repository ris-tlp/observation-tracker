package com.observatory.observationtracker.domain.useraccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import com.observatory.observationtracker.domain.useraccount.dto.UserAccountDtoMapper;
import com.observatory.observationtracker.domain.useraccount.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    private final UserAccountDtoMapper userAccountDtoMapper;
    private final PagedResourcesAssembler<GetUserAccountDto> pagedResourcesAssembler;

    public UserAccountService(UserAccountRepository repository, UserModelAssembler assembler,
                              UserAccountDtoMapper userAccountDtoMapper,
                              PagedResourcesAssembler<GetUserAccountDto> pagedResourcesAssembler
    ) {
        this.repository = repository;
        this.assembler = assembler;
        this.userAccountDtoMapper = userAccountDtoMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public EntityModel<GetUserAccountDto> oneUserByUuid(String uuid) {
        UserAccount account = repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        GetUserAccountDto userAccountDto = userAccountDtoMapper.userAccountToGetDto(account);
        return assembler.toModel(userAccountDto);
    }

    public ResponseEntity<EntityModel<GetUserAccountDto>> createUser(UserAccount newAccount) {
        UserAccount userAccount = repository.save(newAccount);
        GetUserAccountDto userAccountDto = userAccountDtoMapper.userAccountToGetDto(userAccount);
        EntityModel<GetUserAccountDto> entityModel = assembler.toModel(userAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    public ResponseEntity<EntityModel<GetUserAccountDto>> patchUser(String uuid, JsonPatch patch) {
        try {
            UserAccount user =
                    repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
            UserAccount userPatched = applyPatchToUser(patch, user);
            repository.save(userPatched);
            GetUserAccountDto userPatchedDto = userAccountDtoMapper.userAccountToGetDto(userPatched);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(assembler.toModel(userPatchedDto));
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

    public ResponseEntity<PagedModel<EntityModel<GetUserAccountDto>>> getAllUsers(Pageable pageable) {
        Page<GetUserAccountDto> users = repository.findAll(pageable).map(userAccountDtoMapper::userAccountToGetDto);
        PagedModel<EntityModel<GetUserAccountDto>> pagedUsers = pagedResourcesAssembler.toModel(users, assembler);

        return ResponseEntity.status(HttpStatus.OK).body(pagedUsers);
    }


}


@Component
class UserModelAssembler implements RepresentationModelAssembler<GetUserAccountDto, EntityModel<GetUserAccountDto>> {
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
