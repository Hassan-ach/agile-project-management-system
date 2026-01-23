package com.ensa.agile.domain.task.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.task.entity.Task;
import java.util.List;
import java.util.UUID;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface TaskRepository extends BaseDomainRepository<Task, UUID> {
    UUID getProductBackLogIdByTaskId(UUID taskId);
    UUID getSprintIdByTaskId(UUID taskId);
    List<Task> findAllByUserStoryId(UUID userStoryId);
    List<Task> findAllBySprintId(UUID sprintId);
    List<Task> findAllByAssigneeId(UUID assigneeId);
    List<Task> findAllByProductBackLogId(UUID productId);
}
