package com.ensa.agile.infrastructure.persistence.jpa.task.task;

import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.task.history.TaskHistoryJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;
import java.util.function.BiConsumer;

public class TaskJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     * Relationships (assignee, userStory, etc.) remain null to avoid Lazy
     * Loading.
     */
    public static Task toDomain(TaskJpaEntity entity) {
        if (entity == null)
            return null;

        return Task.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .estimatedHours(entity.getEstimatedHours())
            .actualHours(entity.getActualHours())
            // Audit Metadata
            .createdBy(entity.getCreatedBy())
            .createdDate(entity.getCreatedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Use this to map metadata + specific relationships in one
     * go. Usage: TaskJpaMapper.toDomain(entity, TaskJpaMapper::attachAssignee,
     * TaskJpaMapper::attachUserStory);
     */
    @SafeVarargs
    public static Task toDomain(TaskJpaEntity entity,
                                BiConsumer<Task, TaskJpaEntity>... enrichers) {
        Task task = toDomain(entity);
        if (task != null && enrichers != null) {
            for (BiConsumer<Task, TaskJpaEntity> enricher : enrichers) {
                enricher.accept(task, entity);
            }
        }
        return task;
    }

    // 3. Attachers: Logic to load specific relationships
    // These trigger the JPA Fetch when accessed.

    public static void attachAssignee(Task domain, TaskJpaEntity entity) {
        if (entity.getAssignee() != null) {
            domain.setAssignee(UserJpaMapper.toDomain(entity.getAssignee()));
        }
    }

    public static void attachUserStory(Task domain, TaskJpaEntity entity) {
        if (entity.getUserStory() != null) {
            domain.setUserStory(
                UserStoryJpaMapper.toDomain(entity.getUserStory()));
        }
    }

    public static void attachSprintBackLog(Task domain, TaskJpaEntity entity) {
        if (entity.getSprintBackLog() != null) {
            domain.setSprintBackLog(
                SprintBackLogJpaMapper.toDomain(entity.getSprintBackLog()));
        }
    }

    public static void attachStatus(Task domain, TaskJpaEntity entity) {
        if (entity.getStatus() != null) {
            domain.setStatus(TaskHistoryJpaMapper.toDomain(entity.getStatus()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static TaskJpaEntity toJpaEntity(Task domain) {
        if (domain == null)
            return null;

        return TaskJpaEntity.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
            .estimatedHours(domain.getEstimatedHours())
            .actualHours(domain.getActualHours())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: TaskJpaMapper.toJpaEntity(domain, TaskJpaMapper::attachAssignee,
     * TaskJpaMapper::attachStatus);
     */
    @SafeVarargs
    public static TaskJpaEntity
    toJpaEntity(Task domain, BiConsumer<TaskJpaEntity, Task>... enrichers) {
        TaskJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<TaskJpaEntity, Task> enricher : enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers: Logic to set relationships on the JPA Entity

    public static void attachAssignee(TaskJpaEntity entity, Task domain) {
        if (domain.getAssignee() != null) {
            entity.setAssignee(UserJpaMapper.toJpaEntity(domain.getAssignee()));
        }
    }

    public static void attachUserStory(TaskJpaEntity entity, Task domain) {
        if (domain.getUserStory() != null) {
            entity.setUserStory(
                UserStoryJpaMapper.toJpaEntity(domain.getUserStory()));
        }
    }

    public static void attachSprintBackLog(TaskJpaEntity entity, Task domain) {
        if (domain.getSprintBackLog() != null) {
            entity.setSprintBackLog(
                SprintBackLogJpaMapper.toJpaEntity(domain.getSprintBackLog()));
        }
    }

    public static void attachStatus(TaskJpaEntity entity, Task domain) {
        if (domain.getStatus() != null) {
            entity.setStatus(
                TaskHistoryJpaMapper.toJpaEntity(domain.getStatus()));
        }
    }
}
