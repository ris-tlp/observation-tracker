package com.observatory.observationscheduler.observation;

import com.github.fge.jsonpatch.JsonPatch;
import com.observatory.observationscheduler.observation.models.Observation;
import jakarta.websocket.server.PathParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// @TODO have to link observation to a celestial event lmao
@RestController
@RequestMapping(path = "/v1/observations")
public class ObservationController {
    private final ObservationService service;

    public ObservationController(ObservationService service) {
        this.service = service;
    }

    @GetMapping(params = "userUuid")
    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getAllObservationsOfUser(@RequestParam String userUuid) {
        return service.getAllObservations(userUuid);
    }


    @PostMapping(params = "userUuid", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<Observation>> createObservation(@RequestParam String userUuid, @RequestPart(
            "newObservation") Observation newObservation, @RequestPart("images") List<MultipartFile> images) {
        System.out.println(userUuid);
        System.out.println(newObservation);
        System.out.println(images);
        return service.createObservation(newObservation, userUuid, images);
    }

    @GetMapping("/{observationUuid}")
    public ResponseEntity<EntityModel<Observation>> getObservationByUuid(@PathVariable String observationUuid) {
        return service.getObservationByUuid(observationUuid);
    }

    @PatchMapping(path = "/{observationUuid}", consumes = "application/json-patch+json")
    public ResponseEntity<EntityModel<Observation>> patchObservation(@PathVariable String observationUuid,
                                                                     @RequestBody JsonPatch patch) {
        return service.patchObservation(observationUuid, patch);
    }

    @DeleteMapping("/{observationUuid}")
    public ResponseEntity<Void> deleteObservation(@PathVariable String observationUuid) {
        return service.deleteObservation(observationUuid);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Observation>>> getPublishedObservations() {
        return service.getPublishedCourses();
    }
}
