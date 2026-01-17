package com.ensa.agile.domain.sprint.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import java.util.UUID;

public interface SprintBackLogRepository
    extends BaseDomainRepository<SprintBackLog, UUID> {

    UUID getProductBackLogIdBySprintId(UUID sprintId);
}
