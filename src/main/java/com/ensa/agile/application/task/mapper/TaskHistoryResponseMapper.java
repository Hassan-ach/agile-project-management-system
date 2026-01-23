package com.ensa.agile.application.task.mapper;

import com.ensa.agile.application.task.response.TaskHistoryResponse;
import com.ensa.agile.domain.task.entity.TaskHistory;
import java.util.function.BiConsumer;

public class TaskHistoryResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     * Relationships (task) are left null to be handled via attachers.
     */
    public static TaskHistoryResponse toResponse(TaskHistory domain) {
        if (domain == null)
            return null;

        return TaskHistoryResponse.builder()
            .id(domain.getId())
            .status(domain.getStatus() != null ? domain.getStatus() : null)
            .note(domain.getNote())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage:
     * TaskHistoryResponseMapper.toResponse(domain,
     * TaskHistoryResponseMapper::attachTask);
     */
    @SafeVarargs
    public static TaskHistoryResponse
    toResponse(TaskHistory domain,
               BiConsumer<TaskHistoryResponse, TaskHistory>... enrichers) {

        TaskHistoryResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<TaskHistoryResponse, TaskHistory> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }
}
