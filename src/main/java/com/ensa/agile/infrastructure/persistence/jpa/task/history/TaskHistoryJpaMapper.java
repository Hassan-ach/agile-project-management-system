package com.ensa.agile.infrastructure.persistence.jpa.task.history;

import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.infrastructure.persistence.jpa.task.task.TaskJpaMapper;
import java.util.function.BiConsumer;

public class TaskHistoryJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static TaskHistory toDomain(TaskHistoryJpaEntity entity) {
        if (entity == null)
            return null;

        return TaskHistory.builder()
            .id(entity.getId())
            .status(entity.getStatus())
            .note(entity.getNote())
            // Audit Metadata
            .createdBy(entity.getCreatedBy())
            .createdDate(entity.getCreatedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static TaskHistory
    toDomain(TaskHistoryJpaEntity entity,
             BiConsumer<TaskHistory, TaskHistoryJpaEntity>... enrichers) {
        TaskHistory domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<TaskHistory, TaskHistoryJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachTask(TaskHistory domain,
                                  TaskHistoryJpaEntity entity) {
        if (entity.getTask() != null) {
            domain.setTask(TaskJpaMapper.toDomain(entity.getTask()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static TaskHistoryJpaEntity toJpaEntity(TaskHistory domain) {
        if (domain == null)
            return null;

        return TaskHistoryJpaEntity.builder()
            .id(domain.getId())
            .status(domain.getStatus())
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
     */
    @SafeVarargs
    public static TaskHistoryJpaEntity
    toJpaEntity(TaskHistory domain,
                BiConsumer<TaskHistoryJpaEntity, TaskHistory>... enrichers) {
        TaskHistoryJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<TaskHistoryJpaEntity, TaskHistory> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachTask(TaskHistoryJpaEntity entity,
                                  TaskHistory domain) {
        if (domain.getTask() != null) {
            entity.setTask(TaskJpaMapper.toJpaEntity(domain.getTask()));
        }
    }
}
