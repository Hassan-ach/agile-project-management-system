package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.infrastructure.persistence.jpa.task.history.TaskHistoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.task.task.TaskJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestTaskHistoryFactory {
    public static TaskHistoryJpaEntity validTaskHistoryJpaEntity(TaskJpaEntity task, TaskStatus status, UserJpaEntity logedUser) {
        return TaskHistoryJpaEntity.builder()
        .task(task)
            .status(status)
            .note("Valid Task History Note")
            .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<TaskHistoryJpaEntity> multipleTaskHistoryJpaEntities(TaskJpaEntity task, TaskStatus status, UserJpaEntity logedUser, Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validTaskHistoryJpaEntity(task, status, logedUser))
            .toList();
    }
}
