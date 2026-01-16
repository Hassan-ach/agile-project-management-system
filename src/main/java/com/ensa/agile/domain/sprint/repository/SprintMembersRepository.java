package com.ensa.agile.domain.sprint.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintMember;

public interface SprintMembersRepository
    extends BaseDomainRepository<SprintMember, String> {

    boolean existsBySprintBackLogIdAndUserId(String sprintId, String userId);
    void deleteByUserEmailAndSprintBackLogId(String userEmail,
                                             String sprintBackLogId);
}
