package com.ensa.agile.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ensa.agile.application.task.request.TaskCreateRequest;
import com.ensa.agile.application.task.request.TaskGetRequest;
import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.application.task.request.UpdateAssignTaskRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.application.task.response.UpdateAssignTaskResponse;
import com.ensa.agile.application.task.usecase.AssignTaskUseCase;
import com.ensa.agile.application.task.usecase.CreateTaskUseCase;
import com.ensa.agile.application.task.usecase.DeleteTaskUseCase;
import com.ensa.agile.application.task.usecase.GetAllTasksUseCase;
import com.ensa.agile.application.task.usecase.GetTaskUseCase;
import com.ensa.agile.application.task.usecase.GetUserTasksUseCase;
import com.ensa.agile.application.task.usecase.UnAssignTaskUseCase;
import com.ensa.agile.application.task.usecase.UpdateTaskStatusUseCase;
import com.ensa.agile.application.task.usecase.UpdateTaskUseCase;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.enums.TaskStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final AssignTaskUseCase assignTaskUseCase;
    private final UnAssignTaskUseCase unAssignTaskUseCase;
    private final UpdateTaskStatusUseCase updateTaskStatusUseCase;
    private final GetAllTasksUseCase getAllTasksUseCase;
    private final GetUserTasksUseCase getUserTasksUseCase;

    @PostMapping("/stories/{storyId}/tasks")
    public ResponseEntity<TaskResponse>
    createTask(@PathVariable UUID storyId,
               @RequestBody TaskCreateRequest request) {
        TaskResponse response = createTaskUseCase.executeTransactionally(
            new TaskCreateRequest(storyId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse>
    getTask(@PathVariable UUID id,
            @RequestParam(name = "with", required = false) String with) {

        TaskResponse response =
            getTaskUseCase.executeTransactionally(new TaskGetRequest(id, with));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse>
    updateTask( @PathVariable UUID id, @RequestBody TaskUpdateRequest request) {
        TaskResponse response = updateTaskUseCase.executeTransactionally(
            new TaskUpdateRequest(id, request));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteTask(@PathVariable UUID id) {
        deleteTaskUseCase.executeTransactionally(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<UpdateAssignTaskResponse>
    assignTask(@PathVariable UUID id, @RequestBody UpdateAssignTaskRequest request) {

        var req = UpdateAssignTaskRequest.builder()
        .id(id)
        .assigneeEmail(request.getAssigneeEmail())
        .build();

        UpdateAssignTaskResponse response =
            assignTaskUseCase.executeTransactionally(req);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/assignee")
    public ResponseEntity<UpdateAssignTaskResponse>
    unAssignTask(@PathVariable UUID id, @RequestBody UpdateAssignTaskRequest request) {
        var req = UpdateAssignTaskRequest.builder()
        .id(id)
        .assigneeEmail(request.getAssigneeEmail())
        .build();

        UpdateAssignTaskResponse response =
            unAssignTaskUseCase.executeTransactionally(req);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UpdateStatusResponse<TaskHistory>>
    updateTaskStatus(@PathVariable UUID id,
                     @RequestBody UpdateStatusRequest<TaskStatus> request) {

        var req =
        UpdateStatusRequest.<TaskStatus>builder()
           .id(id)
           .status(request.getStatus())
           .note(request.getNote())
           .build();

        UpdateStatusResponse<TaskHistory> response =
            updateTaskStatusUseCase.executeTransactionally(req);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stories/{storyId}/tasks")
    public ResponseEntity<List<TaskResponse>>
    getAllTasks(@PathVariable UUID storyId) {
        List<TaskResponse> response =
        getAllTasksUseCase.executeTransactionally(storyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/me")
    public ResponseEntity<List<TaskResponse>>
    getMyTasks() {
        List<TaskResponse> response =
        getUserTasksUseCase.executeTransactionally(null);
        return ResponseEntity.ok(response);
    }

}
