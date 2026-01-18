package com.ensa.agile.infrastructure.persistence.jpa.story.userstory;

import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserStoryRepository
    extends JpaRepository<UserStoryJpaEntity, UUID> {

    List<UserStoryJpaEntity> findAllByEpic_Id(UUID epicId);

    List<UserStoryJpaEntity> findAllBySprintBackLog_Id(UUID sprintId);

    @Query(""" 
            SELECT u FROM UserStoryJpaEntity u
            WHERE u.id IN :ids
        """)
    List<UserStoryJpaEntity> findByBatch(@Param("ids") List<UUID> ids);

    @Modifying
    @Query(""" 
        UPDATE UserStoryJpaEntity u SET u.sprintBackLog = :sprintBackLog
        WHERE u.id IN :userStoryIds
        """)
    void assignToSprint(List<UUID> userStoryIds,
                        SprintBackLogJpaEntity sprintBackLog);

    @Query("""
        SELECT u.sprintBackLog.id FROM UserStoryJpaEntity u
        WHERE u.id = :userStoryId
        """) Optional<UUID> getSprintBackLogIdByUserStoryId(UUID userStoryId);

    @Query("""
        SELECT u.productBackLog.id FROM UserStoryJpaEntity u
        WHERE u.id = :userStoryId
        """)
    Optional<UUID> getProductBackLogIdByUserStoryId(UUID userStoryId);
}
