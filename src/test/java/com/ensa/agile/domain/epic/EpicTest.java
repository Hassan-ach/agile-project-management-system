package com.ensa.agile.domain.epic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class EpicTest {
    @ParameterizedTest
    @CsvSource({"Epic Title, Epic Description, true",
                "'', Epic Description, false", "Epic Title, '', false",
                "Epic Title, , false", ", Epic Description, false"})
    void shouldValidateEpicCreation(String title, String description,
                                    boolean expectedValidity) {
        if (expectedValidity) {
            assertDoesNotThrow(
                ()
                    -> Epic.builder()
                           .title(title)
                           .description(description)
                           .productBackLog(
                               TestProductBackLogFactory.validProduct())
                           .build());
        } else {
            assertThrows(ValidationException.class, () -> {
                Epic.builder()
                    .title(title)
                    .description(description)
                    .productBackLog(TestProductBackLogFactory.validProduct())
                    .build();
            });
        }
    }

    @Test
    void shouldFailEpicCreationWithoutProductBackLog() {
        assertThrows(ValidationException.class, () -> {
            Epic.builder()
                .title("Epic Title")
                .description("Epic Description")
                .build();
        });
    }

    @ParameterizedTest
    @CsvSource({"Updated Title, Updated Description, true",
                "'', Updated Description, false", "Updated Title, '', false",
                "Updated Title, , true", ", Updated Description, true"})
    void shouldValidateEpicUpdate(String title, String description,
                                  boolean expectedValidity) {
        Epic epic =
            Epic.builder()
                .title("Initial Title")
                .description("Initial Description")
                .productBackLog(TestProductBackLogFactory.validProduct())
                .build();
        if (expectedValidity) {
            assertDoesNotThrow(
                () -> { epic.updateMetadata(title, description); });

            if (title != null)
                assert (epic.getTitle().equals(title));
            if (description != null)
                assert (epic.getDescription().equals(description));
        } else {
            assertThrows(ValidationException.class,
                         () -> { epic.updateMetadata(title, description); });
        }
    }
}
