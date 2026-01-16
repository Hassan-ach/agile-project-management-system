package com.ensa.agile.application.dto.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import org.junit.jupiter.api.Test;
public class GetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully_whenWithIsNull() {
        assertDoesNotThrow(
            () -> { new ProductBackLogGetRequest("123", null); });
    }
}
