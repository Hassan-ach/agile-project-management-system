package com.ensa.agile.domain.sprint.repository;

import java.util.List;
import java.util.UUID;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintHistory;

public interface SprintHistoryRepository
    extends BaseDomainRepository<SprintHistory, UUID> {

    List<SprintHistory> findAllBySprintId(UUID sprintId);
}
