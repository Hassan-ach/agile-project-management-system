package com.ensa.agile.infrastructure.persistence.jpa.sprint.member;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSprintMemberRepository
    extends JpaRepository<SprintMemberJpaEntity, UUID> {
    boolean existsBySprintBackLog_IdAndUser_Id(UUID sprintId, UUID userId);

    void deleteByUser_EmailAndSprintBackLog_Id(String email, UUID sprintId);
}
