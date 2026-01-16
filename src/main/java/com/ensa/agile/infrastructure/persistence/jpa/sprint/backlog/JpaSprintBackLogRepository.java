package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSprintBackLogRepository
    extends JpaRepository<SprintBackLogJpaEntity, UUID> {

    boolean existsByStatus(SprintStatus status);

    @Query("""
        SELECT s.productBackLog.id FROM SprintBackLogJpaEntity s
        WHERE s.id = :sprintId
        """) UUID getProductBackLogIdBySprintId(UUID sprintId);
}
