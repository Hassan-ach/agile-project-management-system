package com.ensa.agile.infrastructure.persistence.jpa.epic;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import java.util.function.BiConsumer;

public class EpicJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static Epic toDomain(EpicJpaEntity entity) {
        if (entity == null)
            return null;

        return Epic.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .description(entity.getDescription())
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
    public static Epic toDomain(EpicJpaEntity entity,
                                BiConsumer<Epic, EpicJpaEntity>... enrichers) {
        Epic domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<Epic, EpicJpaEntity> enricher : enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachProductBackLog(Epic domain, EpicJpaEntity entity) {
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
    public static EpicJpaEntity toJpaEntity(Epic domain) {
        if (domain == null)
            return null;

        return EpicJpaEntity.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
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
    public static EpicJpaEntity
    toJpaEntity(Epic domain, BiConsumer<EpicJpaEntity, Epic>... enrichers) {
        EpicJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<EpicJpaEntity, Epic> enricher : enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachProductBackLog(EpicJpaEntity entity, Epic domain) {
        if (domain.getProductBackLog() != null) {
            entity.setProductBackLog(ProductBackLogJpaMapper.toJpaEntity(
                domain.getProductBackLog()));
        }
    }
}
