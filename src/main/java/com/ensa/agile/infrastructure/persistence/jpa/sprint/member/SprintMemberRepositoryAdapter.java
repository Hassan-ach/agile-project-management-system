package com.ensa.agile.infrastructure.persistence.jpa.sprint.member;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ensa.agile.application.sprint.exception.SprintMemberNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintMember;
import com.ensa.agile.domain.sprint.repository.SprintMembersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SprintMemberRepositoryAdapter implements SprintMembersRepository {
    private final JpaSprintMemberRepository jpaSprintMemberRepository;

    @Override
    public SprintMember save(SprintMember entity) {
        return SprintMemberJpaMapper.toDomainEntity(
            this.jpaSprintMemberRepository.save(
                SprintMemberJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public SprintMember findById(UUID s) {
        return this.jpaSprintMemberRepository.findById(s)
            .map(SprintMemberJpaMapper::toDomainEntity)
            .orElseThrow(SprintMemberNotFoundException::new);
    }

    @Override
    public List<SprintMember> findAll() {
        return this.jpaSprintMemberRepository.findAll()
            .stream()
            .map(SprintMemberJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        this.jpaSprintMemberRepository.deleteById(s);
    }

    @Override
    public boolean existsById(UUID s) {
        return this.jpaSprintMemberRepository.existsById(s);
    }

    @Override
    public boolean existsBySprintBackLogIdAndUserId(UUID sprintId,
                                                    UUID userId) {
        return this.jpaSprintMemberRepository
            .existsBySprintBackLog_IdAndUser_Id(sprintId, userId);
    }

    @Override
    public void deleteByUserEmailAndSprintBackLogId(String email,
                                                    UUID sprintId) {
        this.jpaSprintMemberRepository.deleteByUser_EmailAndSprintBackLog_Id(
            email, sprintId);
    }

    @Override
    public void deleteByUserIdAndSprintBackLogId(UUID userId,
                                                    UUID sprintId) {
        this.jpaSprintMemberRepository.deleteByUser_IdAndSprintBackLog_Id(
            userId, sprintId);
    }
}
