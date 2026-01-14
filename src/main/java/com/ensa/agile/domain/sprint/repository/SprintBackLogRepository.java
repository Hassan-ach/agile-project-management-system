package com.ensa.agile.domain.sprint.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.enums.SprintStatus;

public interface SprintBackLogRepository
    extends BaseDomainRepository<SprintBackLog, String> {

    boolean existsByStatus(SprintStatus status);
    String getProductBackLogIdBySprintId(String sprintId);
}
