package com.observatory.observationtracker;

import com.observatory.observationtracker.domain.celestialevent.models.CelestialEvent;
import com.observatory.observationtracker.domain.observation.ObservationController;
import com.observatory.observationtracker.domain.observation.ObservationService;
import com.observatory.observationtracker.domain.observation.dto.GetObservationDto;
import com.observatory.observationtracker.domain.observation.dto.ObservationDtoMapper;
import com.observatory.observationtracker.domain.observation.models.Observation;
import com.observatory.observationtracker.domain.useraccount.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebMvcTest(ObservationController.class)
public class ObservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObservationService observationService;

    @MockBean
    private ObservationDtoMapper mapper;

    @Test
    void doSomething() {
        UserAccount user = new UserAccount("Name", "Email");
        CelestialEvent event = new CelestialEvent("Name", "Description", LocalDateTime.of(LocalDate.of(2055, 11
                , 11), LocalTime.now()));
        Observation observation = new Observation(
                "Name", "Description",
                user,
                LocalDateTime.of(LocalDate.of(2055, 11
                , 11), LocalTime.now()), event);

        GetObservationDto returnDto = mapper.observationToGetDto(observation);


//        when(observationService.getObservationByUuid(user.getUuid())).thenReturn( ResponseEntity.ok().body(returnDto));
    }


}
