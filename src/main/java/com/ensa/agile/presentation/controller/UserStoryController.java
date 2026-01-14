package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.story.request.UserStoryCreateRequest;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.request.UserStoryUpdateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.application.story.usecase.CreateUserStoryUseCase;
import com.ensa.agile.application.story.usecase.DeleteUserStoryUseCase;
import com.ensa.agile.application.story.usecase.GetUserStoryUseCase;
import com.ensa.agile.application.story.usecase.UpdateUserStoryUseCase;
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
@RequestMapping("/api/{productId}/user-story")
public class UserStoryController {

    private final CreateUserStoryUseCase createUserStoryUseCase;
    private final GetUserStoryUseCase getUserStoryUseCase;
    private final UpdateUserStoryUseCase updateUserStoryUseCase;
    private final DeleteUserStoryUseCase deleteUserStoryUseCase;

    @PostMapping
    public ResponseEntity<UserStoryResponse>
    createUserStory(@RequestBody UserStoryCreateRequest request,
                    @PathVariable String productId) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createUserStoryUseCase.executeTransactionally(
                new UserStoryCreateRequest(productId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryResponse> getUserStoryById(
        @PathVariable String productId, @PathVariable String id,
        @RequestParam(name = "with", required = false) String with) {

        return ResponseEntity.ok().body(
            getUserStoryUseCase.executeTransactionally(
                new UserStoryGetRequest(id, with)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserStoryResponse>
    updateUserStory(@PathVariable String productId, @PathVariable String id,
                    @RequestBody UserStoryUpdateRequest request) {

        return ResponseEntity.ok().body(
            updateUserStoryUseCase.executeTransactionally(
                new UserStoryUpdateRequest(productId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse>
    deleteUserStory(@PathVariable String productId, @PathVariable String id) {
        deleteUserStoryUseCase.executeTransactionally(id);
        return ResponseEntity.noContent().build();
    }
}
