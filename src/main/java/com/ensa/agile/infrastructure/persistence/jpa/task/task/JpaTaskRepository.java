package com.ensa.agile.infrastructure.persistence.jpa.task.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskJpaEntity, UUID> {
    @Query("""
        SELECT t.userStory.sprintBackLog.productBackLog.id FROM TaskJpaEntity t
        WHERE t.id = :taskId
        """) Optional<UUID> getProductBackLogIdByTaskId(UUID taskId);

    @Query("""
        SELECT t.sprintBackLog.id FROM TaskJpaEntity t
        WHERE t.id = :taskId
        """) Optional<UUID> geSprintIdByTaskId(UUID taskId);

    List<TaskJpaEntity> findAllByUserStory_Id(UUID userStoryId);
    List<TaskJpaEntity> findAllByAssignee_Id(UUID assigneeId);
    List<TaskJpaEntity> findAllBySprintBackLog_Id(UUID sprintId);
}
