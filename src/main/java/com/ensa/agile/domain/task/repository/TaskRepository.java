package com.ensa.agile.domain.task.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.task.entity.Task;

public interface TaskRepository extends BaseDomainRepository<Task, String> {
    //
    String getProductBackLogIdByTaskId(String taskId);
}
