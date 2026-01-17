package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public final class TestProductBackLogFactory {
    public static ProductBackLog validProduct() {
        return ProductBackLog.builder()
            .name("Test Product")
            .description("This is a test product backlog.")
            .build();
    }
    public static ProductBackLog validProductWithId() {
        UUID id = UUID.randomUUID();

        return ProductBackLog.builder()
            .id(id)
            .name("Test Product")
            .description("This is a test product backlog.")
            .build();
    }

    public static ProductBackLogJpaEntity validJpaProduct(UserJpaEntity logedUser) {
        return ProductBackLogJpaEntity.builder()
            .name("Test Product")
            .description("This is a test product backlog.")
        .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<ProductBackLogJpaEntity> multipleJpaProducts(UserJpaEntity logedUser, Integer count) {
        return IntStream.range(0, count)
            .mapToObj(i -> validJpaProduct(logedUser))
            .toList();
    }
}
