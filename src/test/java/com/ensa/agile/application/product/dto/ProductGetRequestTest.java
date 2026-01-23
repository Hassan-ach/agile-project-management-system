package com.ensa.agile.application.product.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
public class ProductGetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully_whenWithIsNull() {
        assertDoesNotThrow(
            () -> { new ProductBackLogGetRequest(UUID.randomUUID(), null); });
    }
}
