package com.ensa.agile.infrastructure.persistence.jpa.product.member;

import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;

public class ProjectMemberJpaMapper {

    public static ProjectMemberJpaEntity
    toJpaEntity(ProjectMember projectMember) {
        if (projectMember == null) {
            return null;
        }
        return ProjectMemberJpaEntity.builder()
                .id(projectMember.getId())
                .user( projectMember.getUser() == null ? null :
                UserJpaMapper.toJpaEntity(projectMember.getUser()))
                .productBackLog( projectMember.getProductBackLog() == null ? null :
                ProductBackLogJpaMapper.toJpaEntity(
                    projectMember.getProductBackLog()))
                .role(projectMember.getRole())
                .status(projectMember.getStatus())
                .build();
    }
    public static ProjectMember
    toDomainEntity(ProjectMemberJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return ProjectMember.builder()
            .id(jpaEntity.getId())
            .user(jpaEntity.getUser() == null ? null :
            UserJpaMapper.toDomainEntity(jpaEntity.getUser()))
            .productBackLog(jpaEntity.getProductBackLog() == null ? null :
            ProductBackLogJpaMapper.toDomainEntity(
                jpaEntity.getProductBackLog()))
            .role(jpaEntity.getRole())
            .status(jpaEntity.getStatus())
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }
}
