package com.ensa.agile.application.task.mapper;

import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.task.entity.Task;
import java.util.function.BiConsumer;

public class TaskResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     * Relationships (assignee) are left null to be handled via attachers.
     */
    public static TaskResponse toResponse(Task domain) {
        if (domain == null)
            return null;

        return TaskResponse.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
            .estimatedHours(domain.getEstimatedHours())
            .actualHours(domain.getActualHours())
            .status(domain.getStatus() != null ? domain.getStatus().getStatus()
                                               : null)
            .latestHistory(
                TaskHistoryResponseMapper.toResponse(domain.getStatus()))
            // mappable or Audit entity
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: TaskResponseMapper.toResponse(domain,
     * TaskResponseMapper::attachAssignee);
     */
    @SafeVarargs
    public static TaskResponse
    toResponse(Task domain, BiConsumer<TaskResponse, Task>... enrichers) {
        TaskResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<TaskResponse, Task> enricher : enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachAssignee(TaskResponse response, Task domain) {
        if (domain.getAssignee() != null) {
            response.setAssignee(domain.getAssignee().getEmail());
        }
    }
}
