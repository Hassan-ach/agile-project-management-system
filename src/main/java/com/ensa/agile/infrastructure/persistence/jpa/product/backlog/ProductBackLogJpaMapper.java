package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.domain.product.entity.ProductBackLog;
import java.util.function.BiConsumer;

public class ProductBackLogJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static ProductBackLog toDomain(ProductBackLogJpaEntity entity) {
        if (entity == null)
            return null;

        return ProductBackLog.builder()
            .id(entity.getId())
            .name(entity.getName())
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
    public static ProductBackLog
    toDomain(ProductBackLogJpaEntity entity,
             BiConsumer<ProductBackLog, ProductBackLogJpaEntity>... enrichers) {
        ProductBackLog domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<ProductBackLog, ProductBackLogJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    // No specific relationships mapped in source input.
    // Add methods here (e.g., attachUserStories) if OneToMany mapping is
    // required.

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static ProductBackLogJpaEntity toJpaEntity(ProductBackLog domain) {
        if (domain == null)
            return null;

        return ProductBackLogJpaEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
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
    public static ProductBackLogJpaEntity toJpaEntity(
        ProductBackLog domain,
        BiConsumer<ProductBackLogJpaEntity, ProductBackLog>... enrichers) {
        ProductBackLogJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<ProductBackLogJpaEntity, ProductBackLog> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }
}
