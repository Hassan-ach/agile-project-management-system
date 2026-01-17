package com.ensa.agile.application.product.request;

import com.ensa.agile.domain.global.exception.ValidationException;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductBackLogUpdateRequest {

    private UUID id;
    private String name;
    private String description;

    public ProductBackLogUpdateRequest(UUID id,
                                       ProductBackLogUpdateRequest req) {

        if (req == null) {
            throw new ValidationException("Request cannot be null");
        }

        if (req.getName() == null && req.getDescription() == null) {
            throw new ValidationException(
                "At least one field must be provided for update");
        }

        if (req.getName() != null) {
            if (req.getName().isBlank()) {
                throw new ValidationException("Name cannot be blank");
            } else {
                this.name = req.getName();
            }
        }

        if (req.getDescription() != null) {
            if (req.getDescription().isBlank()) {
                throw new ValidationException("Description cannot be blank");
            } else {
                this.description = req.getDescription();
            }
        }

        if (id == null ) {
            throw new ValidationException("ID cannot be null");
        }

        this.id = id;
    }
}
