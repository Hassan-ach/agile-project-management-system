package com.ensa.agile.infrastructure.persistence.jpa.product.member;

import com.ensa.agile.application.product.exception.ProjectMemberNotFoundException;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectMemberRepositoryAdapter implements ProjectMemberRepository {
    private final JpaProjectMemberRepository jpaProjectMemberRepository;

    @Override
    public ProjectMember save(ProjectMember projectMember) {
        return ProjectMemberJpaMapper.toDomain(jpaProjectMemberRepository.save(
            ProjectMemberJpaMapper.toJpaEntity(projectMember)));
    }

    @Override
    public ProjectMember findById(UUID id) {
        return ProjectMemberJpaMapper.toDomain(
            jpaProjectMemberRepository.findById(id).orElseThrow(
                ProjectMemberNotFoundException::new));
    }

    @Override
    public List<ProjectMember> findAll() {
        return jpaProjectMemberRepository.findAll()
            .stream()
            .map(ProjectMemberJpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(UUID projectMemberId) {
        jpaProjectMemberRepository.deleteById(projectMemberId);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaProjectMemberRepository.existsById(id);
    }

    @Override
    public boolean existsByProductBackLogIdAndUserId(UUID productBackLogId,
                                                     UUID userId) {
        return jpaProjectMemberRepository.existsByUser_IdAndProductBackLog_Id(
            userId, productBackLogId);
    }

    @Override
    public boolean existsByUserIdAndProductBackLogId(UUID userId,
                                                     UUID productBackLogId) {
        return jpaProjectMemberRepository.existsByUser_IdAndProductBackLog_Id(
            userId, productBackLogId);
    }
    @Override
    public boolean existsByUserEmailAndProductBackLogId(String userEmail,
                                                        UUID productBackLogId) {
        return jpaProjectMemberRepository
            .existsByUser_EmailAndProductBackLog_Id(userEmail,
                                                    productBackLogId);
    }

    @Override
    public boolean
    existsByUserIdAndProductBackLogIdAndRole(UUID userId, UUID productBackLogId,
                                             RoleType role) {
        return jpaProjectMemberRepository
            .existsByUser_IdAndProductBackLog_IdAndRole(userId,
                                                        productBackLogId, role);
    }

    @Override
    public void deleteByUserIdAndProductBackLogId(UUID userId,
                                                  UUID productBackLogId) {

        jpaProjectMemberRepository.deleteByUser_IdAndProductBackLog_Id(
            userId, productBackLogId);
    }

    @Override
    public void deleteByUserEmailAndProductBackLogId(String userEmail,
                                                     UUID productBackLogId) {

        jpaProjectMemberRepository.deleteByUser_EmailAndProductBackLog_Id(
            userEmail, productBackLogId);
    }

    @Override
    public boolean existsByUserEmailAndProductBackLogIdAndRole(
        String userEmail, UUID productBackLogId, RoleType role) {

        return jpaProjectMemberRepository
            .existsByUser_EmailAndProductBackLog_IdAndRole(
                userEmail, productBackLogId, role);
    }

    @Override
    public ProjectMember
    findByUserIdAndProductBackLogId(UUID userId, UUID productBackLogId) {
        return ProjectMemberJpaMapper.toDomain(
            jpaProjectMemberRepository
                .findByUser_IdAndProductBackLog_Id(userId, productBackLogId)
                .orElseThrow(ProjectMemberNotFoundException::new));
    }

    @Override
    public List<ProjectMember> findAllByProductBackLogId(UUID productId) {
        return jpaProjectMemberRepository.findAllByProductBackLog_Id(productId)
            .stream()
            .map(ProjectMemberJpaMapper::toDomain)
            .toList();
    }
}
