package com.ensa.agile.application.dto.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.product.request.ProductBackLogUpdateRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class UpdateRequestTest {
    @Test
    void shouldCreateProductBackLogUpdateRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(() -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name("Updated Backlog")
                           .description("This is an updated product backlog.")
                           .build());
        });
    }

    @Test
    void shouldUpdateOnlyNameSuccessfully_whenDescriptionIsNull() {
        assertDoesNotThrow(() -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name("Updated Backlog")
                           .description(null)
                           .build());
        });
    }

    @Test
    void shouldUpdateOnlyDescriptionSuccessfully_whenNameIsNull() {
        assertDoesNotThrow(() -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name(null)
                           .description("This is an updated product backlog.")
                           .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenBothFieldsAreNull() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name(null)
                           .description(null)
                           .build());
        });
    }
    @Test
    void shouldThrowValidationException_whenNameIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name(" ")
                           .description("This is an updated product backlog.")
                           .build());
        });
    }

    @Test
    void shouldThrowValidationException_whenDescriptionIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogUpdateRequest(
                "123", ProductBackLogUpdateRequest.builder()
                           .name("Updated Backlog")
                           .description(" ")
                           .build());
        });
    }
    @Test
    void shouldThrowValidationException_whenUpdateRequestIsNull() {
        assertThrows(ValidationException.class,
                     () -> { new ProductBackLogUpdateRequest("123", null); });
    }

    @Test
    void shouldThrowValidationException_whenIdIsNull() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogUpdateRequest(
                null, ProductBackLogUpdateRequest.builder()
                          .name("Updated Backlog")
                          .description("This is an updated product backlog.")
                          .build());
        });
    }
    @Test
    void shouldThrowValidationException_whenIdIsBlank() {
        assertThrows(ValidationException.class, () -> {
            new ProductBackLogUpdateRequest(
                " ", ProductBackLogUpdateRequest.builder()
                         .name("Updated Backlog")
                         .description("This is an updated product backlog.")
                         .build());
        });
    }
}
