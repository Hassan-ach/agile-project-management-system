package com.ensa.agile.domain.sprint.repository;

import java.util.UUID;

import com.ensa.agile.domain.global.annotation.Loggable;
import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintMember;

@Loggable
public interface SprintMembersRepository
    extends BaseDomainRepository<SprintMember, UUID> {

    boolean existsBySprintBackLogIdAndUserId(UUID sprintId, UUID userId);
    void deleteByUserEmailAndSprintBackLogId(String userEmail,
                                             UUID sprintBackLogId);
    void deleteByUserIdAndSprintBackLogId(UUID userId,
                                             UUID sprintBackLogId);
}
