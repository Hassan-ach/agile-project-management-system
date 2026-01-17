package com.ensa.agile.domain.product.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.RoleType;
import java.util.UUID;

public interface ProjectMemberRepository
    extends BaseDomainRepository<ProjectMember, UUID> {

    boolean existsByProductBackLogIdAndUserId(UUID projectId, UUID userId);

    boolean existsByUserEmailAndProductBackLogId(String userEmail,
                                                 UUID productBackLogId);

    boolean existsByUserIdAndProductBackLogId(UUID userId,
                                                 UUID productBackLogId);

    boolean existsByUserIdAndProductBackLogIdAndRole(UUID userId,
                                                     UUID productBackLogId,
                                                     RoleType role);

    boolean existsByUserEmailAndProductBackLogIdAndRole(String userEmail,
                                                        UUID productBackLogId,
                                                        RoleType role);
    void deleteByUserEmailAndProductBackLogId(String userEmail,
                                              UUID productBackLogId);

    void deleteByUserIdAndProductBackLogId(UUID userId,
                                              UUID productBackLogId);

    ProjectMember findByUserIdAndProductBackLogId(UUID userId,
                                                  UUID productBackLogId);
}
