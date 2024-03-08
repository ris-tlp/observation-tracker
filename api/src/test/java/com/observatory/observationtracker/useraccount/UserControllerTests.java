package com.observatory.observationtracker.useraccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationtracker.domain.useraccount.UserAccountController;
import com.observatory.observationtracker.domain.useraccount.UserAccountModelAssembler;
import com.observatory.observationtracker.domain.useraccount.UserAccountService;
import com.observatory.observationtracker.domain.useraccount.dto.CreateUserAccountDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private UserAccountModelAssembler userAccountModelAssembler;

    @MockBean
    private PagedResourcesAssembler<GetUserAccountDto> pagedResourcesAssembler;

    @Autowired
    private ObjectMapper mapper;

    @InjectMocks
    private UserAccountController userAccountController;

    private String uuid;
    private String email;
    private String name;
    private String baseUrl;
    private GetUserAccountDto getDto;
    private CreateUserAccountDto createDto;
    private EntityModel<GetUserAccountDto> getDtoEntity;

    @Before
    public void setUp() {
        uuid = "1234";
        name = "name";
        email = "email@email.com";
        baseUrl = "/v1/users";

        getDto = new GetUserAccountDto();
        getDto.setUuid(uuid);
        getDto.setEmail(email);
        getDto.setName(name);

        createDto = new CreateUserAccountDto();
        createDto.setName(name);
        createDto.setEmail(email);

        getDtoEntity = EntityModel.of(getDto)
                .add(
                        linkTo(methodOn(UserAccountController.class).getOneUserByUuid(getDto.getUuid())).withSelfRel().withType("GET, PATCH"),
                        linkTo(methodOn(UserAccountController.class).createUser(createDto)).withRel("user").withType(
                                "POST")
                );

    }


    @Test
    public void getOneUserByUuidTest() throws Exception {
        when(userAccountService.oneUserByUuid(uuid)).thenReturn(getDto);
        when(userAccountModelAssembler.toModel(getDto)).thenReturn(getDtoEntity);


        mockMvc.perform(get("/v1/users/{uuid}", uuid)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(getDto.getName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(getDto.getEmail()))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.uuid").value(getDto.getUuid()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").value(baseUrl + "/" + getDto.getUuid()))
                .andExpect(jsonPath("$._links.self.type").exists())
                .andExpect(jsonPath("$._links.self.type").value("GET, PATCH"))
                .andExpect(jsonPath("$._links.user.href").exists())
                .andExpect(jsonPath("$._links.user.href").value(baseUrl))
                .andExpect(jsonPath("$._links.user.type").exists())
                .andExpect(jsonPath("$._links.user.type").value("POST"));

    }


    @Test
    public void createUserTest() throws Exception {
        when(userAccountService.createUser(createDto)).thenReturn(getDto);
        when(userAccountModelAssembler.toModel(getDto)).thenReturn(getDtoEntity);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(getDto.getName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(getDto.getEmail()))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").value(baseUrl + "/" + getDto.getUuid()))
                .andExpect(jsonPath("$._links.self.type").exists())
                .andExpect(jsonPath("$._links.self.type").value("GET, PATCH"))
                .andExpect(jsonPath("$._links.user.href").exists())
                .andExpect(jsonPath("$._links.user.href").value(baseUrl))
                .andExpect(jsonPath("$._links.user.type").exists())
                .andExpect(jsonPath("$._links.user.type").value("POST"));
    }

    @Test
    public void patchUserIsNotNullTest() throws Exception {
        String replacedName = "Replaced name";
        Map<String, String> jsonPatch = new HashMap<>();
        jsonPatch.put("op", "replace");
        jsonPatch.put("path", "/name");
        jsonPatch.put("value", replacedName);

        getDto.setName(replacedName);
        when(userAccountService.patchUser(anyString(), any(JsonPatch.class))).thenReturn(getDto);
        when(userAccountModelAssembler.toModel(getDto)).thenReturn(getDtoEntity);

        mockMvc.perform(patch("/v1/users/{uuid}", uuid)
                        .contentType("application/json-patch+json")
                        .content(mapper.writeValueAsString(List.of(jsonPatch)))
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(getDto.getName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(getDto.getEmail()))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").value(baseUrl + "/" + getDto.getUuid()))
                .andExpect(jsonPath("$._links.self.type").exists())
                .andExpect(jsonPath("$._links.self.type").value("GET, PATCH"))
                .andExpect(jsonPath("$._links.user.href").exists())
                .andExpect(jsonPath("$._links.user.href").value(baseUrl))
                .andExpect(jsonPath("$._links.user.type").exists())
                .andExpect(jsonPath("$._links.user.type").value("POST"));


    }

    @Test
    public void patchUserIsNullTest() throws Exception {
        String replacedName = "Replaced name";
        Map<String, String> jsonPatch = new HashMap<>();
        jsonPatch.put("op", "replace");
        jsonPatch.put("path", "/name");
        jsonPatch.put("value", replacedName);

        getDto = null;
        when(userAccountService.patchUser(anyString(), any(JsonPatch.class))).thenReturn(getDto);
        when(userAccountModelAssembler.toModel(getDto)).thenReturn(getDtoEntity);

        mockMvc.perform(patch("/v1/users/{uuid}", uuid)
                        .contentType("application/json-patch+json")
                        .content(mapper.writeValueAsString(List.of(jsonPatch)))
                )
                .andExpect(status().is(500));
    }
}
