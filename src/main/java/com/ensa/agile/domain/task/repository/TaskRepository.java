package com.ensa.agile.domain.task.repository;

import java.util.UUID;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.task.entity.Task;

public interface TaskRepository extends BaseDomainRepository<Task, UUID> {
    UUID getProductBackLogIdByTaskId(UUID taskId);
}
