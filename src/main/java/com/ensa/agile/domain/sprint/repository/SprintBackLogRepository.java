package com.ensa.agile.domain.sprint.repository;

import com.ensa.agile.domain.global.annotation.Loggable;
import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import java.util.List;
import java.util.UUID;
@Loggable
public interface SprintBackLogRepository
    extends BaseDomainRepository<SprintBackLog, UUID> {

    UUID getProductBackLogIdBySprintId(UUID sprintId);
    List<SprintBackLog> findAllByProductBackLogId(UUID productId);
}
