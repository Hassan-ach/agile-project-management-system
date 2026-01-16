package com.ensa.agile.application.epic.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EpicCreateRequest {

    private String title;

    private String description;

    private UUID productId;

    public EpicCreateRequest(UUID productId, EpicCreateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        if (productId == null) {
            throw new ValidationException("productId cannot be null");
        }
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ValidationException("title cannot be null or blank");
        }
        if (req.getDescription() == null || req.getDescription().isBlank()) {
            throw new ValidationException(
                "description cannot be null or blank");
        }

        this.title = req.getTitle();
        this.description = req.getDescription();
        this.productId = productId;
    }
}
