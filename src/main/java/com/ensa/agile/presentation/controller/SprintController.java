package com.ensa.agile.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensa.agile.application.common.request.UpdateStatusRequest;
import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.common.response.UpdateStatusResponse;
import com.ensa.agile.application.sprint.request.SprintBackLogCreateRequest;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.application.sprint.request.UpdateStorySprintRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogProgressResponse;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.sprint.response.SprintBurndownChartResponse;
import com.ensa.agile.application.sprint.usecase.AddStoryToSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.CreateSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.DeleteSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.GetSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.GetSprintBurndownChartUseCase;
import com.ensa.agile.application.sprint.usecase.GetSprintHistoryUseCase;
import com.ensa.agile.application.sprint.usecase.GetSprintProgressUseCase;
import com.ensa.agile.application.sprint.usecase.RemoveStoryFromSprintBackLogUseCase;
import com.ensa.agile.application.sprint.usecase.UpdateSprintBackLogStatusUseCase;
import com.ensa.agile.application.sprint.usecase.UpdateSprintBackLogUseCase;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SprintController {
    private final CreateSprintBackLogUseCase createSprintBackLogUseCase;
    private final UpdateSprintBackLogUseCase updateSprintBackLogUseCase;
    private final GetSprintBackLogUseCase getSprintBackLogUseCase;
    private final DeleteSprintBackLogUseCase deleteSprintBackLogUseCase;
    private final UpdateSprintBackLogStatusUseCase updateSprintBackLogStatusUseCase;
    private final AddStoryToSprintBackLogUseCase addStoryToSprintBackLogUseCase;
    private final RemoveStoryFromSprintBackLogUseCase removeStoryFromSprintBackLogUseCase;
    private final GetSprintHistoryUseCase getSprintHistoryUseCase;
    private final GetSprintBurndownChartUseCase getSprintBurndownChartUseCase;
    private final GetSprintProgressUseCase getSprintProgressUseCase;

    @PostMapping("/projects/{projectId}/sprints")
    @PreAuthorize("@abacService.canAccessSprint(#projectId, null, 'CREATE')")
    public ResponseEntity<SprintBackLogResponse>
    createSprint(@PathVariable UUID projectId, @RequestBody SprintBackLogCreateRequest request) {

        SprintBackLogResponse response =
            createSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogCreateRequest(projectId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/sprints/{id}")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'VIEW')")
    public ResponseEntity<SprintBackLogResponse>
    getSprint(@PathVariable UUID id,
              @RequestParam(name = "with", required = false) String with) {

        SprintBackLogResponse response =
            getSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogGetRequest(id, with));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/sprints/{id}")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'UPDATE')")
    public ResponseEntity<SprintBackLogResponse>
    updateSprint(@PathVariable UUID id,
                 @RequestBody SprintBackLogUpdateRequest request) {

        SprintBackLogResponse response =
            updateSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogUpdateRequest(id, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/sprints/{id}")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'DELETE')")
    public ResponseEntity<DeleteResponse>
    deleteSprint(@PathVariable UUID id) {

        deleteSprintBackLogUseCase.executeTransactionally(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/sprints/{id}/status")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'UPDATE_STATUS')")
    public ResponseEntity<UpdateStatusResponse<SprintHistory>>
    updateSprintStatus(@PathVariable UUID id,
                       @RequestBody UpdateStatusRequest<SprintStatus> request) {
        var req = UpdateStatusRequest.<SprintStatus>builder()
            .id(id)
            .status(request.getStatus())
            .note(request.getNote())
            .build();

        var response =
            updateSprintBackLogStatusUseCase.executeTransactionally(req);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/sprints/{id}/stories/{storyId}")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'MANAGE_STORIES')")
    public ResponseEntity<SprintBackLogResponse>
    addStoryToSprint(@PathVariable UUID id,
                     @PathVariable UUID storyId) {
        var request = UpdateStorySprintRequest.builder()
            .id(id)
            .storyId(storyId)
            .build();

        SprintBackLogResponse response =
            addStoryToSprintBackLogUseCase.executeTransactionally(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/sprints/{id}/stories/{storyId}")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'MANAGE_STORIES')")
    public ResponseEntity<SprintBackLogResponse>
    removeStoryFromSprint(@PathVariable UUID id,
                          @PathVariable UUID storyId) {

        var request = UpdateStorySprintRequest.builder()
            .id(id)
            .storyId(storyId)
            .build();

        SprintBackLogResponse response =
            removeStoryFromSprintBackLogUseCase.executeTransactionally(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/sprints/{id}/backlog")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'VIEW_BACKLOG')")
    public ResponseEntity<SprintBackLogResponse>
    getSprintBacklog(@PathVariable UUID id) {

        SprintBackLogResponse response =
            getSprintBackLogUseCase.executeTransactionally(
                new SprintBackLogGetRequest(id, "ALL"));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/sprints/{id}/history")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'VIEW_HISTORY')")
    public ResponseEntity<List<SprintHistory>>
    getSprintHistory(@PathVariable UUID id) {

        List<SprintHistory> response =
        getSprintHistoryUseCase.executeTransactionally(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sprints/{id}/burndown")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'VIEW_BURNDOWN')")
    public ResponseEntity<SprintBurndownChartResponse> getSprintBurndownChart(@PathVariable UUID id) {

        var response = getSprintBurndownChartUseCase.executeTransactionally(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sprints/{id}/progress")
    @PreAuthorize("@abacService.canAccessSprint(null, #id, 'VIEW_PROGRESS')")
    public ResponseEntity<SprintBackLogProgressResponse> getSprintProgress(@PathVariable UUID id) {
        var response = getSprintProgressUseCase.executeTransactionally(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("sprints/{id}/report")
    @PreAuthorize("@abacService.canViewReport(null, #id, 'SPRINT')")
    public ResponseEntity<?> getSprintReport(@PathVariable UUID id) {
        return ResponseEntity.ok("Sprint report generation is not implemented yet.");
    }

}
