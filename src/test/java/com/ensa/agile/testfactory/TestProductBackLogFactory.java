package com.ensa.agile.testfactory;

import com.ensa.agile.domain.product.entity.ProductBackLog;
import java.util.UUID;

public final class TestProductBackLogFactory {
    public static ProductBackLog validProduct() {
        return ProductBackLog.builder()
            .name("Test Product")
            .description("This is a test product backlog.")
            .build();
    }
    public static ProductBackLog validProductWithId() {
        String id = UUID.randomUUID().toString();

        return ProductBackLog.builder()
            .id(id)
            .name("Test Product")
            .description("This is a test product backlog.")
            .build();
    }
}
