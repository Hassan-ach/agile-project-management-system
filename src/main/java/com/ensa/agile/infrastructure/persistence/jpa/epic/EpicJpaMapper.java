package com.ensa.agile.infrastructure.persistence.jpa.epic;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;

public class EpicJpaMapper {
    public static EpicJpaEntity toJpaEntity(Epic epic) {

        return EpicJpaEntity.builder()
            .id(epic.getId())
            .title(epic.getTitle())
            .description(epic.getDescription())
            .productBackLog(
                ProductBackLogJpaMapper.toJpaEntity(epic.getProductBackLog()))
            .createdDate(epic.getCreatedDate())
            .createdBy(epic.getCreatedBy())
            .lastModifiedDate(epic.getLastModifiedDate())
            .lastModifiedBy(epic.getLastModifiedBy())
            .build();
    }

    public static Epic toDomainEntity(EpicJpaEntity epic) {

        return Epic.builder()
            .id(epic.getId())
            .title(epic.getTitle())
            .description(epic.getDescription())
            .productBackLog(ProductBackLogJpaMapper.toDomainEntity(
                epic.getProductBackLog()))
            .createdDate(epic.getCreatedDate())
            .createdBy(epic.getCreatedBy())
            .lastModifiedDate(epic.getLastModifiedDate())
            .lastModifiedBy(epic.getLastModifiedBy())
            .build();
    }
}
