package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.task.request.TaskCreateRequest;
import com.ensa.agile.application.task.request.TaskGetRequest;
import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.application.task.usecase.CreateTaskUseCase;
import com.ensa.agile.application.task.usecase.DeleteTaskUseCase;
import com.ensa.agile.application.task.usecase.GetTaskUseCase;
import com.ensa.agile.application.task.usecase.UpdateTaskUseCase;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{producId}/{spritId}/{storyId}/tasks")
public class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskResponse>
    createTask(@PathVariable UUID productId, @PathVariable UUID sprintId,
               @PathVariable UUID storyId,
               @RequestBody TaskCreateRequest request) {
        TaskResponse response = createTaskUseCase.executeTransactionally(
            new TaskCreateRequest(sprintId, storyId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse>
    getTask(@PathVariable UUID id,
            @RequestParam(name = "with", required = false) String with) {
        TaskResponse response =
            getTaskUseCase.executeTransactionally(new TaskGetRequest(id, with));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse>
    updateTask(@PathVariable UUID sprintId, @PathVariable UUID storyId,
               @PathVariable UUID id, @RequestBody TaskUpdateRequest request) {
        TaskResponse response = updateTaskUseCase.executeTransactionally(
            new TaskUpdateRequest(sprintId, storyId, id, request));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteTask(@PathVariable UUID id) {
        deleteTaskUseCase.executeTransactionally(id);

        return ResponseEntity.noContent().build();
    }
}
