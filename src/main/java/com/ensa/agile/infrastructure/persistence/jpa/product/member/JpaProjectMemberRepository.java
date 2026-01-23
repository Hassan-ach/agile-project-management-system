package com.ensa.agile.infrastructure.persistence.jpa.product.member;

import com.ensa.agile.domain.product.enums.RoleType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectMemberRepository
    extends JpaRepository<ProjectMemberJpaEntity, UUID> {

    boolean existsByUser_IdAndProductBackLog_Id(UUID userId,
                                                UUID productBackLogId);

    boolean existsByUser_EmailAndProductBackLog_Id(String userEmail,
                                                   UUID productBackLogId);
    boolean existsByUser_IdAndProductBackLog_IdAndRole(UUID userId,
                                                       UUID productBackLogId,
                                                       RoleType role);

    boolean existsByUser_EmailAndProductBackLog_IdAndRole(String userEmail,
                                                          UUID productBackLogId,
                                                          RoleType role);

    void deleteByUser_IdAndProductBackLog_Id(UUID userId,
                                             UUID productBackLogId);
    void deleteByUser_EmailAndProductBackLog_Id(String userEmail,
                                                UUID productBackLogId);

    Optional<ProjectMemberJpaEntity>
    findByUser_IdAndProductBackLog_Id(UUID userId, UUID productBackLogId);
    List<ProjectMemberJpaEntity> findAllByProductBackLog_Id(UUID projectId);
}
