package com.ensa.agile.application.task.mapper;

import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.task.entity.Task;

public class TaskResponseMapper {

    public static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .assignee(task.getAssignee().getEmail())
            .estimatedHours(task.getEstimatedHours())
            .status(task.getStatus())
            .actualHours(task.getActualHours())
            .createdDate(task.getCreatedDate())
            .createdBy(task.getCreatedBy())
            .lastModifiedDate(task.getLastModifiedDate())
            .lastModifiedBy(task.getLastModifiedBy())
            .build();
    }
    public static TaskResponse toResponse(Task task, String assigneeEmail) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .assignee(assigneeEmail)
            .estimatedHours(task.getEstimatedHours())
            .status(task.getStatus())
            .actualHours(task.getActualHours())
            .createdDate(task.getCreatedDate())
            .createdBy(task.getCreatedBy())
            .lastModifiedDate(task.getLastModifiedDate())
            .lastModifiedBy(task.getLastModifiedBy())
            .build();
    }
}
