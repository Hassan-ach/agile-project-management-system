package com.ensa.agile.application.common.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.application.common.request.GetRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void shouldCreateGetRequestSuccessfully_whenValidInput() {
        assertDoesNotThrow(
            () -> { new GetRequest("123", "field1, field2, field3"); });
    }

    @Test
    void shouldCreateGetRequestSuccessfully_whenWithIsBlank() {
        assertDoesNotThrow(() -> { new GetRequest("123", " "); });
    }

    @Test
    void shouldThrowValidationException_whenIdIsNull() {
        assertThrows(ValidationException.class,
                     () -> { new GetRequest(null, "FIELD1, field2"); });
    }

    @Test
    void shouldTrimAndUpperCaseFieldsSuccessfully() {
        GetRequest request = new GetRequest("123", " field1 , FIELD2, FiElD3 ");
        assert (request.getFields().size() == 3);
        assert (request.getFields().get(0).equals("FIELD1"));
        assert (request.getFields().get(1).equals("FIELD2"));
        assert (request.getFields().get(2).equals("FIELD3"));
    }
}
