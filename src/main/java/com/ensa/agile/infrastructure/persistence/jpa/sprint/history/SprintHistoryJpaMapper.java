package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import java.util.function.BiConsumer;

public class SprintHistoryJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintHistory toDomain(SprintHistoryJpaEntity entity) {
        if (entity == null)
            return null;

        return SprintHistory.builder()
            .id(entity.getId())
            .status(entity.getStatus())
            .note(entity.getNote())
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
    public static SprintHistory
    toDomain(SprintHistoryJpaEntity entity,
             BiConsumer<SprintHistory, SprintHistoryJpaEntity>... enrichers) {
        SprintHistory domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<SprintHistory, SprintHistoryJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachSprint(SprintHistory domain,
                                    SprintHistoryJpaEntity entity) {
        if (entity.getSprint() != null) {
            domain.setSprint(
                SprintBackLogJpaMapper.toDomain(entity.getSprint()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintHistoryJpaEntity toJpaEntity(SprintHistory domain) {
        if (domain == null)
            return null;

        return SprintHistoryJpaEntity.builder()
            .id(domain.getId())
            .status(domain.getStatus())
            .note(domain.getNote())
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
    public static SprintHistoryJpaEntity toJpaEntity(
        SprintHistory domain,
        BiConsumer<SprintHistoryJpaEntity, SprintHistory>... enrichers) {
        SprintHistoryJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<SprintHistoryJpaEntity, SprintHistory> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachSprint(SprintHistoryJpaEntity entity,
                                    SprintHistory domain) {
        if (domain.getSprint() != null) {
            entity.setSprint(
                SprintBackLogJpaMapper.toJpaEntity(domain.getSprint()));
        }
    }
}
