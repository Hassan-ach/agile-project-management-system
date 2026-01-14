package com.ensa.agile.infrastructure.persistence.jpa.epic;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEpicRepository
    extends JpaRepository<EpicJpaEntity, String> {
    List<EpicJpaEntity> findAllByProductBackLog_Id(String projectId);

    @Query("""
    SELECT new com.ensa.agile.infrastructure.persistence.jpa.epic.EpicRow(
        e.id,
        e.title,
        e.description,

        u.id,
        u.title,
        u.description,
        u.priority,
        u.storyPoints,
        u.acceptanceCriteria
    )
    FROM EpicJpaEntity e
    LEFT JOIN UserStoryJpaEntity u ON u.epic.id = e.id
    WHERE e.id = ?1
    """) List<EpicRow> loadEpicRowsById(String id);

    @Query("""
        SELECT e.productBackLog.id FROM EpicJpaEntity e
        WHERE e.id = :epicId
        """) Optional<String> getProductBackLogIdByEpicId(String epicId);
}
