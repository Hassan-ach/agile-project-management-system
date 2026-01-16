package com.ensa.agile.domain.sprint.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintMember;
import java.util.UUID;

public interface SprintMembersRepository
    extends BaseDomainRepository<SprintMember, UUID> {

    boolean existsBySprintBackLogIdAndUserId(UUID sprintId, UUID userId);
    void deleteByUserEmailAndSprintBackLogId(String userEmail,
                                             UUID sprintBackLogId);
}
