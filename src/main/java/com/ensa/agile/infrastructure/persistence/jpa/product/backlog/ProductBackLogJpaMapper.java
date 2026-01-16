package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.domain.product.entity.ProductBackLog;

public class ProductBackLogJpaMapper {

    public static ProductBackLogJpaEntity
    toJpaEntity(ProductBackLog productBackLog) {
        if (productBackLog == null) {
            return null;
        }

        ProductBackLogJpaEntity jpaEntity =
            ProductBackLogJpaEntity.builder()
                .id(productBackLog.getId())
                .name(productBackLog.getName())
                .description(productBackLog.getDescription())
                .createdDate(productBackLog.getCreatedDate())
                .createdBy(productBackLog.getCreatedBy())
                .lastModifiedDate(productBackLog.getLastModifiedDate())
                .lastModifiedBy(productBackLog.getLastModifiedBy())
                .build();
        return jpaEntity;
    }

    public static ProductBackLog
    toDomainEntity(ProductBackLogJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return ProductBackLog.builder()
            .id(jpaEntity.getId())
            .name(jpaEntity.getName())
            .description(jpaEntity.getDescription())
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }
}
