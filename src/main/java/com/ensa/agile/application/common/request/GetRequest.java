package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class GetRequest {

    private UUID id;
    private List<String> fields;

    public GetRequest(UUID id, String with) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        this.id = id;
        if (with == null || with.isBlank()) {
            this.fields = List.of();
        } else {
            this.fields = List.of(with.split(","))
                              .stream()
                              .map(String::trim)
                              .map(String::toUpperCase)
                              .toList();
        }
    }
}
