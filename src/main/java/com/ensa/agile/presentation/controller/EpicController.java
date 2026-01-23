package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.epic.request.EpicCreateRequest;
import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.request.EpicRequest;
import com.ensa.agile.application.epic.request.EpicUpdateRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.epic.usecase.CreateEpicUseCase;
import com.ensa.agile.application.epic.usecase.DeleteEpicUseCase;
import com.ensa.agile.application.epic.usecase.GetAllEpicsUseCase;
import com.ensa.agile.application.epic.usecase.GetEpicUseCase;
import com.ensa.agile.application.epic.usecase.UpdateEpicUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EpicController {

    private final CreateEpicUseCase createEpicUseCase;
    private final UpdateEpicUseCase updateEpicUseCase;
    private final DeleteEpicUseCase deleteEpicUseCase;
    private final GetEpicUseCase getEpicUseCase;
    private final GetAllEpicsUseCase getAllEpicsUseCase;

    @PostMapping("/projects/{projectId}/epics")
    @PreAuthorize("@abacService.canAccessEpic(#projectId, null, 'CREATE')")
    public ResponseEntity<EpicResponse>
    createEpic(@PathVariable UUID projectId,
               @RequestBody EpicCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createEpicUseCase.executeTransactionally(
                new EpicCreateRequest(projectId, request)));
    }

    @GetMapping("/projects/{projectId}/epics")
    @PreAuthorize("@abacService.canAccessEpic(#projectId, null, 'VIEW_ALL')")
    public ResponseEntity<List<EpicResponse>>
    getAllEpics(@PathVariable UUID projectId) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(getAllEpicsUseCase.executeTransactionally(projectId));
    }

    @GetMapping("/epics/{id}")
    @PreAuthorize("@abacService.canAccessEpic(null, #id, 'VIEW')")
    public ResponseEntity<EpicResponse>
    getEpicById(@PathVariable UUID id,
                @RequestParam(name = "with", required = false) String with) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(getEpicUseCase.executeTransactionally(
                new EpicGetRequest(id, with)));
    }

    @PutMapping("/epics/{id}")
    @PreAuthorize("@abacService.canAccessEpic(null, #id, 'UPDATE')")
    public ResponseEntity<EpicResponse>
    updateEpic(@PathVariable UUID id, @RequestBody EpicUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(updateEpicUseCase.executeTransactionally(
                new EpicUpdateRequest(id, request)));
    }

    @DeleteMapping("/epics/{id}")
    @PreAuthorize("@abacService.canAccessEpic(null, #id, 'DELETE')")
    public ResponseEntity<DeleteResponse> deleteEpic(@PathVariable UUID id) {

        deleteEpicUseCase.executeTransactionally(new EpicRequest(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
