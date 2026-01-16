package com.ensa.agile.infrastructure.persistence.jpa.epic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEpicRepository extends JpaRepository<EpicJpaEntity, UUID> {
    List<EpicJpaEntity> findAllByProductBackLog_Id(UUID projectId);

    @Query("""
        SELECT e.productBackLog.id FROM EpicJpaEntity e
        WHERE e.id = :epicId
        """) Optional<UUID> getProductBackLogIdByEpicId(UUID epicId);
}
