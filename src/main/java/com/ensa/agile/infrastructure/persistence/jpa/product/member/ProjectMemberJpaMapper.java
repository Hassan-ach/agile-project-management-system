package com.ensa.agile.infrastructure.persistence.jpa.product.member;

import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;
import java.util.function.BiConsumer;

public class ProjectMemberJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static ProjectMember toDomain(ProjectMemberJpaEntity entity) {
        if (entity == null)
            return null;

        return ProjectMember.builder()
            .id(entity.getId())
            .role(entity.getRole())
            .status(entity.getStatus())
            // Audit Metadata
            .createdBy(entity.getCreatedBy())
            .createdDate(entity.getCreatedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static ProjectMember
    toDomain(ProjectMemberJpaEntity entity,
             BiConsumer<ProjectMember, ProjectMemberJpaEntity>... enrichers) {
        ProjectMember domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<ProjectMember, ProjectMemberJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachUser(ProjectMember domain,
                                  ProjectMemberJpaEntity entity) {
        if (entity.getUser() != null) {
            domain.setUser(UserJpaMapper.toDomain(entity.getUser()));
        }
    }

    public static void attachProductBackLog(ProjectMember domain,
                                            ProjectMemberJpaEntity entity) {
        if (entity.getProductBackLog() != null) {
            domain.setProductBackLog(
                ProductBackLogJpaMapper.toDomain(entity.getProductBackLog()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static ProjectMemberJpaEntity toJpaEntity(ProjectMember domain) {
        if (domain == null)
            return null;

        return ProjectMemberJpaEntity.builder()
            .id(domain.getId())
            .role(domain.getRole())
            .status(domain.getStatus())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static ProjectMemberJpaEntity toJpaEntity(
        ProjectMember domain,
        BiConsumer<ProjectMemberJpaEntity, ProjectMember>... enrichers) {
        ProjectMemberJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<ProjectMemberJpaEntity, ProjectMember> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachUser(ProjectMemberJpaEntity entity,
                                  ProjectMember domain) {
        if (domain.getUser() != null) {
            entity.setUser(UserJpaMapper.toJpaEntity(domain.getUser()));
        }
    }

    public static void attachProductBackLog(ProjectMemberJpaEntity entity,
                                            ProjectMember domain) {
        if (domain.getProductBackLog() != null) {
            entity.setProductBackLog(ProductBackLogJpaMapper.toJpaEntity(
                domain.getProductBackLog()));
        }
    }
}
