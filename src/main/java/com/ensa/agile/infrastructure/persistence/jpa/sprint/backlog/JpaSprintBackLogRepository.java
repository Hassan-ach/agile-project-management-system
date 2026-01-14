package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSprintBackLogRepository
    extends JpaRepository<SprintBackLogJpaEntity, String> {

    boolean existsByStatus(SprintStatus status);

    @Query("""
        SELECT s.productBackLog.id FROM SprintBackLogJpaEntity s
        WHERE s.id = :sprintId
        """) String getProductBackLogIdBySprintId(String sprintId);
}
