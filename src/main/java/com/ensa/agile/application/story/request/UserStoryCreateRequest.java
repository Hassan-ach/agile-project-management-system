package com.ensa.agile.application.story.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.story.enums.MoscowType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserStoryCreateRequest {

    private String title;
    private String description;
    private MoscowType priority;
    private Integer storyPoints;
    private String acceptanceCriteria;
    private UUID productId;

    // This constructor is for validation purposes
    public UserStoryCreateRequest(UUID productId, UserStoryCreateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }

        if (productId == null) {
            throw new ValidationException("productId cannot be null or blank");
        }

        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ValidationException("title cannot be null or blank");
        }
        if (req.getDescription() == null || req.getDescription().isBlank()) {
            throw new ValidationException(
                "description cannot be null or blank");
        }
        if (req.getPriority() == null) {
            throw new ValidationException("priority cannot be null");
        }
        if (req.getStoryPoints() == null || req.getStoryPoints() < 1) {
            throw new ValidationException(
                "storyPoints cannot be null or less than 1");
        }
        if (req.getAcceptanceCriteria() == null ||
            req.getAcceptanceCriteria().isBlank()) {
            throw new ValidationException(
                "acceptanceCriteria cannot be null or blank");
        }
        if (req.getAcceptanceCriteria().length() > 1000) {
            throw new ValidationException(
                "acceptanceCriteria cannot exceed 1000 characters");
        }

        this.productId = productId;
        this.title = req.getTitle();
        this.description = req.getDescription();
        this.priority = req.getPriority();
        this.storyPoints = req.getStoryPoints();
        this.acceptanceCriteria = req.getAcceptanceCriteria();
    }
}
