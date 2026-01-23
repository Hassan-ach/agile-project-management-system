package com.ensa.agile.infrastructure.persistence.jpa.sprint.member;

import com.ensa.agile.domain.sprint.entity.SprintMember;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;
import java.util.function.BiConsumer;

public class SprintMemberJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintMember toDomain(SprintMemberJpaEntity entity) {
        if (entity == null)
            return null;

        return SprintMember.builder()
            .id(entity.getId())
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
    public static SprintMember
    toDomain(SprintMemberJpaEntity entity,
             BiConsumer<SprintMember, SprintMemberJpaEntity>... enrichers) {
        SprintMember domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<SprintMember, SprintMemberJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachUser(SprintMember domain,
                                  SprintMemberJpaEntity entity) {
        if (entity.getUser() != null) {
            domain.setUser(UserJpaMapper.toDomain(entity.getUser()));
        }
    }

    public static void attachSprintBackLog(SprintMember domain,
                                           SprintMemberJpaEntity entity) {
        if (entity.getSprintBackLog() != null) {
            domain.setSprintBackLog(
                SprintBackLogJpaMapper.toDomain(entity.getSprintBackLog()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintMemberJpaEntity toJpaEntity(SprintMember domain) {
        if (domain == null)
            return null;

        return SprintMemberJpaEntity.builder()
            .id(domain.getId())
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
    public static SprintMemberJpaEntity
    toJpaEntity(SprintMember domain,
                BiConsumer<SprintMemberJpaEntity, SprintMember>... enrichers) {
        SprintMemberJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<SprintMemberJpaEntity, SprintMember> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachUser(SprintMemberJpaEntity entity,
                                  SprintMember domain) {
        if (domain.getUser() != null) {
            entity.setUser(UserJpaMapper.toJpaEntity(domain.getUser()));
        }
    }

    public static void attachSprintBackLog(SprintMemberJpaEntity entity,
                                           SprintMember domain) {
        if (domain.getSprintBackLog() != null) {
            entity.setSprintBackLog(
                SprintBackLogJpaMapper.toJpaEntity(domain.getSprintBackLog()));
        }
    }
}
