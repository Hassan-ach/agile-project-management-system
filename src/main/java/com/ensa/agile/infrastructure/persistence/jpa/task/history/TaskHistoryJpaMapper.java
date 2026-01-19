package com.ensa.agile.infrastructure.persistence.jpa.task.history;

import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.infrastructure.persistence.jpa.task.task.TaskJpaMapper;

public class TaskHistoryJpaMapper {
    public static TaskHistoryJpaEntity toJpaEntity(TaskHistory entity) {
        if (entity == null) {
            return null;
        }
        return TaskHistoryJpaEntity.builder()
            .id(entity.getId())
            .task(TaskJpaMapper.toJpaEntity(entity.getTask()))
            .status(entity.getStatus())
            .note(entity.getNote())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }

    public static TaskHistory toDomainEntity(TaskHistoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return TaskHistory.builder()
            .id(jpaEntity.getId())
            .task(TaskJpaMapper.toDomainEntity(jpaEntity.getTask()))
            .status(jpaEntity.getStatus())
            .note(jpaEntity.getNote())
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }
}
