package com.observatory.observationtracker.domain.useraccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import com.observatory.observationtracker.domain.useraccount.dto.UserAccountDtoMapper;
import com.observatory.observationtracker.domain.useraccount.exceptions.UserNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserAccountService {
    private final UserAccountRepository repository;
    private final UserAccountDtoMapper userAccountDtoMapper;

    public UserAccountService(UserAccountRepository repository,
                              UserAccountDtoMapper userAccountDtoMapper
    ) {
        this.repository = repository;
        this.userAccountDtoMapper = userAccountDtoMapper;
    }

    public GetUserAccountDto oneUserByUuid(String uuid) {
        UserAccount account = repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        return userAccountDtoMapper.userAccountToGetDto(account);
    }

    public GetUserAccountDto createUser(UserAccount newAccount) {
        UserAccount userAccount = repository.save(newAccount);
        GetUserAccountDto userDto = userAccountDtoMapper.userAccountToGetDto(userAccount);

        return userDto;
    }

    public GetUserAccountDto patchUser(String uuid, JsonPatch patch) {
        try {
            UserAccount user =
                    repository.findUserAccountByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
            UserAccount userPatched = applyPatchToUser(patch, user);
            repository.save(userPatched);
            GetUserAccountDto userPatchedDto = userAccountDtoMapper.userAccountToGetDto(userPatched);
            return userPatchedDto;
        } catch (JsonPatchException | JsonProcessingException exception) {
            return null;
        }
    }

    private UserAccount applyPatchToUser(JsonPatch patch, UserAccount user) throws JsonPatchException,
            JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(user, JsonNode.class));
        return mapper.treeToValue(patched, UserAccount.class);
    }

    public Page<GetUserAccountDto> getAllUsers(Pageable pageable) {
        Page<GetUserAccountDto> usersDto = repository.findAll(pageable).map(userAccountDtoMapper::userAccountToGetDto);
        return usersDto;
    }
}


