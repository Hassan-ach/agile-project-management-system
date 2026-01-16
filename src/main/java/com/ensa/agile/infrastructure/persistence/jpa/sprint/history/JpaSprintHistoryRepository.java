package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSprintHistoryRepository
    extends JpaRepository<SprintHistoryJpaEntity, UUID> {}
