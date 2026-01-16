package com.ensa.agile.application.dto.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.product.request.ProductBackLogCreateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;
public class CreateRequestTest {

    @Test
    void shouldCreateProductBackLogCreateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new ProductBackLogCreateRequest(
                ProductBackLogCreateRequest.builder()
                    .name("Sample Backlog")
                    .description("This is a sample product backlog.")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNameIsNull() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogCreateRequest(
                ProductBackLogCreateRequest.builder()
                    .name(null)
                    .description("This is a sample product backlog.")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenNameIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogCreateRequest(
                ProductBackLogCreateRequest.builder()
                    .name(" ")
                    .description("This is a sample product backlog.")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenDescriptionIsNull() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogCreateRequest(
                ProductBackLogCreateRequest.builder()
                    .name("Sample Backlog")
                    .description(null)
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenDescriptionIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogCreateRequest(
                ProductBackLogCreateRequest.builder()
                    .name("Sample Backlog")
                    .description(" ")
                    .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class,
                     () -> { new ProductBackLogCreateRequest(null); });
    }
}
