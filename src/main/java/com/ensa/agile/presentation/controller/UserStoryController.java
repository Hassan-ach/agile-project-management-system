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
import com.ensa.agile.application.story.request.UserStoryCreateRequest;
import com.ensa.agile.application.story.request.UserStoryEpicUpdateRequest;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.request.UserStoryUpdatePriorityRequest;
import com.ensa.agile.application.story.request.UserStoryUpdateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.application.story.usecase.CreateUserStoryUseCase;
import com.ensa.agile.application.story.usecase.DeleteUserStoryUseCase;
import com.ensa.agile.application.story.usecase.GetUserStoryHistoryUseCase;
import com.ensa.agile.application.story.usecase.GetUserStoryUseCase;
import com.ensa.agile.application.story.usecase.LinkStoryToEpicUseCase;
import com.ensa.agile.application.story.usecase.UnLinkStoryToEpicUseCase;
import com.ensa.agile.application.story.usecase.UpdateUserStoryPriorityUseCase;
import com.ensa.agile.application.story.usecase.UpdateUserStoryStatusUseCase;
import com.ensa.agile.application.story.usecase.UpdateUserStoryUseCase;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.enums.StoryStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class UserStoryController {

    private final CreateUserStoryUseCase createUserStoryUseCase;
    private final GetUserStoryUseCase getUserStoryUseCase;
    private final UpdateUserStoryUseCase updateUserStoryUseCase;
    private final DeleteUserStoryUseCase deleteUserStoryUseCase;
    private final UpdateUserStoryStatusUseCase updateUserStoryStatusUseCase;
    private final LinkStoryToEpicUseCase linkStoryToEpicUseCase;
    private final UnLinkStoryToEpicUseCase unLinkStoryToEpicUseCase;
    private final UpdateUserStoryPriorityUseCase updateUserStoryPriorityUseCase;
    private final GetUserStoryHistoryUseCase getUserStoryHistoryUseCase;

    @PostMapping("/projects/{projectId}/stories")
    @PreAuthorize("@abacService.canAccessStory(#projectId, null, 'CREATE')")
    public ResponseEntity<UserStoryResponse>
    createUserStory(@RequestBody UserStoryCreateRequest request,
                    @PathVariable UUID projectId) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createUserStoryUseCase.executeTransactionally(
                new UserStoryCreateRequest(projectId, request)));
    }

    @GetMapping("/stories/{id}")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'VIEW')")
    public ResponseEntity<UserStoryResponse> getUserStoryById(@PathVariable UUID id,
        @RequestParam(name = "with", required = false) String with) {

        return ResponseEntity.ok().body(
            getUserStoryUseCase.executeTransactionally(
                new UserStoryGetRequest(id, with)));
    }

    @PutMapping("/stories/{id}")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'UPDATE')")
    public ResponseEntity<UserStoryResponse>
    updateUserStory(@PathVariable UUID id,
                    @RequestBody UserStoryUpdateRequest request) {

        return ResponseEntity.ok().body(
            updateUserStoryUseCase.executeTransactionally(
                new UserStoryUpdateRequest(id, request)));
    }

    @DeleteMapping("/stories/{id}")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'DELETE')")
    public ResponseEntity<DeleteResponse>
    deleteUserStory(@PathVariable UUID id) {
        deleteUserStoryUseCase.executeTransactionally(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/stories/{id}/status")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'UPDATE_STATUS')")
    public ResponseEntity<UpdateStatusResponse<UserStoryHistory>>
    updateUserStoryStatus( @PathVariable UUID id,
                          @RequestBody UpdateStatusRequest<StoryStatus> request) {

        var req =
        UpdateStatusRequest.<StoryStatus>builder()
           .id(id)
           .status(request.getStatus())
           .note(request.getNote())
           .build();

        return ResponseEntity.ok().body(
            updateUserStoryStatusUseCase.executeTransactionally(req));
    }

    @PatchMapping("/stories/{id}/epic/{epicId}")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'LINK_TO_EPIC')")
    public ResponseEntity<UserStoryResponse>
    linkUserStoryToEpic(@PathVariable UUID id,
                        @PathVariable UUID epicId) {

            var req = UserStoryEpicUpdateRequest.builder()
                .id(id)
                .epicId(epicId)
                .build();

        return ResponseEntity.ok().body(
            linkStoryToEpicUseCase.executeTransactionally(req));
    }

    @DeleteMapping("/stories/{id}/epic/{epicId}")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'UNLINK_TO_EPIC')")
    public ResponseEntity<UserStoryResponse>
    unLinkUserStoryToEpic(@PathVariable UUID id,
                          @PathVariable UUID epicId) {
        var req = UserStoryEpicUpdateRequest.builder()
            .id(id)
            .epicId(epicId)
            .build();

        return ResponseEntity.ok().body(
            unLinkStoryToEpicUseCase.executeTransactionally(req));
    }

    @PatchMapping("/stories/{id}/priority")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'UPDATE_PRIORITY')")
    public ResponseEntity<UserStoryResponse>
    updateStoryPriority(@RequestBody UserStoryUpdatePriorityRequest request) {
        var req = UserStoryUpdatePriorityRequest.builder()
            .id(request.getId())
            .priority(request.getPriority())
            .build();

        return ResponseEntity.ok().body(
            updateUserStoryPriorityUseCase.executeTransactionally(req)
        );
    }

    @GetMapping("/stories/{id}/history")
    @PreAuthorize("@abacService.canAccessStory(null, #id, 'VIEW_HISTORY')")
    public ResponseEntity<List<UserStoryHistory>>
    getUserStoryHistory(@PathVariable UUID id) {

        List<UserStoryHistory> history =
            getUserStoryHistoryUseCase.executeTransactionally(id);

        return ResponseEntity.ok().body(history);
    }

}
