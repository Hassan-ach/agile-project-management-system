package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.sprint.request.SprintBackLogCreateRequest;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.sprint.usecase.CreateSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.DeleteSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.GetSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.UpdateSprintBackLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{productId}/sprints")
public class SprintController {
    private final CreateSprintBackLogUseCase createSprintBackLogUseCase;
    private final UpdateSprintBackLogUseCase updateSprintBackLogUseCase;
    private final GetSprintBackLogUseCase getSprintBackLogUseCase;
    private final DeleteSprintBackLogUseCase deleteSprintBackLogUseCase;

    @PostMapping
    public ResponseEntity<SprintBackLogResponse>
    createSprint(@RequestBody SprintBackLogCreateRequest request) {

        SprintBackLogResponse response =
            createSprintBackLogUseCase.executeTransactionally(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{sprintId}")
    public ResponseEntity<SprintBackLogResponse>
    getSprint(@PathVariable String sprintId,
              @RequestParam(name = "with", required = false) String with) {

        SprintBackLogResponse response =
            getSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogGetRequest(sprintId, with));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{sprintId}")
    public ResponseEntity<SprintBackLogResponse>
    updateSprint(@PathVariable String sprintId,
                 @RequestBody SprintBackLogUpdateRequest request) {
        SprintBackLogResponse response =
            updateSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogUpdateRequest(sprintId, request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse>
    deleteSprint(@PathVariable String sprintId) {
        deleteSprintBackLogUseCase.executeTransactionally(sprintId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
