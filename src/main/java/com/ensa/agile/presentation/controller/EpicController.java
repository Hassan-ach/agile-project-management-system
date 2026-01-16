package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.epic.request.EpicCreateRequest;
import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.request.EpicRequest;
import com.ensa.agile.application.epic.request.EpicUpdateRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.epic.usecase.CreateEpicUseCase;
import com.ensa.agile.application.epic.usecase.DeleteEpicUseCase;
import com.ensa.agile.application.epic.usecase.GetEpicUseCase;
import com.ensa.agile.application.epic.usecase.UpdateEpicUseCase;
import java.util.UUID;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{productId}/epics")
public class EpicController {

    private final CreateEpicUseCase createEpicUseCase;
    private final UpdateEpicUseCase updateEpicUseCase;
    private final DeleteEpicUseCase deleteEpicUseCase;
    private final GetEpicUseCase getEpicUseCase;

    @PostMapping
    public ResponseEntity<EpicResponse>
    createEpic(@PathVariable UUID productId,
               @RequestBody EpicCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createEpicUseCase.executeTransactionally(
                new EpicCreateRequest(productId, request)));
    }

    @GetMapping("/{epicId}")
    public ResponseEntity<EpicResponse>
    getEpicById(@PathVariable UUID productId, @PathVariable UUID epicId,
                @RequestParam String with) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(getEpicUseCase.executeTransactionally(
                new EpicGetRequest(productId, epicId, with)));
    }

    @PatchMapping("/{epicId}")
    public ResponseEntity<EpicResponse>
    updateEpic(@PathVariable UUID productId, @PathVariable UUID epicId,
               @RequestBody EpicUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(updateEpicUseCase.executeTransactionally(
                new EpicUpdateRequest(productId, epicId, request)));
    }

    @DeleteMapping("/{epicId}")
    public ResponseEntity<DeleteResponse>
    deleteEpic(@PathVariable UUID productId, @PathVariable UUID epicId) {

        deleteEpicUseCase.executeTransactionally(
            new EpicRequest(epicId, productId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
