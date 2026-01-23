package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.history.SprintHistoryJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;
import java.util.function.BiConsumer;

public class SprintBackLogJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintBackLog toDomain(SprintBackLogJpaEntity entity) {
        if (entity == null)
            return null;

        return SprintBackLog.builder()
            .id(entity.getId())
            .name(entity.getName())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .goal(entity.getGoal())
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
    public static SprintBackLog
    toDomain(SprintBackLogJpaEntity entity,
             BiConsumer<SprintBackLog, SprintBackLogJpaEntity>... enrichers) {
        SprintBackLog domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<SprintBackLog, SprintBackLogJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachProductBackLog(SprintBackLog domain,
                                            SprintBackLogJpaEntity entity) {
        if (entity.getProductBackLog() != null) {
            domain.setProductBackLog(
                ProductBackLogJpaMapper.toDomain(entity.getProductBackLog()));
        }
    }

    public static void attachScrumMaster(SprintBackLog domain,
                                         SprintBackLogJpaEntity entity) {
        if (entity.getScrumMaster() != null) {
            domain.setScrumMaster(
                UserJpaMapper.toDomain(entity.getScrumMaster()));
        }
    }

    public static void attachStatus(SprintBackLog domain,
                                    SprintBackLogJpaEntity entity) {
        if (entity.getStatus() != null) {
            domain.setStatus(
                SprintHistoryJpaMapper.toDomain(entity.getStatus()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintBackLogJpaEntity toJpaEntity(SprintBackLog domain) {
        if (domain == null)
            return null;

        return SprintBackLogJpaEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .startDate(domain.getStartDate())
            .endDate(domain.getEndDate())
            .goal(domain.getGoal())
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
    public static SprintBackLogJpaEntity toJpaEntity(
        SprintBackLog domain,
        BiConsumer<SprintBackLogJpaEntity, SprintBackLog>... enrichers) {
        SprintBackLogJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<SprintBackLogJpaEntity, SprintBackLog> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachProductBackLog(SprintBackLogJpaEntity entity,
                                            SprintBackLog domain) {
        if (domain.getProductBackLog() != null) {
            entity.setProductBackLog(ProductBackLogJpaMapper.toJpaEntity(
                domain.getProductBackLog()));
        }
    }

    public static void attachScrumMaster(SprintBackLogJpaEntity entity,
                                         SprintBackLog domain) {
        if (domain.getScrumMaster() != null) {
            entity.setScrumMaster(
                UserJpaMapper.toJpaEntity(domain.getScrumMaster()));
        }
    }

    public static void attachStatus(SprintBackLogJpaEntity entity,
                                    SprintBackLog domain) {
        if (domain.getStatus() != null) {
            entity.setStatus(
                SprintHistoryJpaMapper.toJpaEntity(domain.getStatus()));
        }
    }
}
