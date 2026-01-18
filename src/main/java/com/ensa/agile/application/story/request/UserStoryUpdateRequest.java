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
public class UserStoryUpdateRequest {

    private UUID id;
    private String title;
    private String description;
    private MoscowType priority;
    private Integer storyPoints;
    private String acceptanceCriteria;
    private UUID sprintId;
    private UUID epicId;

    // This constructor is for validation purposes
    public UserStoryUpdateRequest(UUID id,
                                  UserStoryUpdateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }

        if (id == null) {
            throw new ValidationException("id cannot be null or blank");
        }

        if (req.getTitle() == null && req.getDescription() == null &&
            req.getPriority() == null && req.getStoryPoints() == null &&
            req.getAcceptanceCriteria() == null && req.getSprintId() == null) {
            throw new ValidationException(
                "At least one field must be provided for update");
        }
        if (req.getSprintId() != null) {
                this.sprintId = req.getSprintId();
        }

        if (req.getTitle() != null) {
            if (req.getTitle().isBlank()) {
                throw new ValidationException("title cannot be blank");
            } else {
                this.title = req.getTitle();
            }
        }

        if (req.getDescription() != null) {
            if (req.getDescription().isBlank()) {
                throw new ValidationException("description cannot be blank");
            } else {
                this.description = req.getDescription();
            }
        }

        if (req.getPriority() != null) {
            this.priority = req.getPriority();
        }

        if (req.getStoryPoints() != null) {
            if (req.getStoryPoints() < 1) {
                throw new ValidationException(
                    "storyPoints cannot be less than 1");
            } else {
                this.storyPoints = req.getStoryPoints();
            }
        }

        if (req.getAcceptanceCriteria() != null) {
            if (req.getAcceptanceCriteria().isBlank()) {
                throw new ValidationException(
                    "acceptanceCriteria cannot be blank");
            } else {
                this.acceptanceCriteria = req.getAcceptanceCriteria();
            }
        }

        this.id = id;
        this.epicId = req.getEpicId();
    }
}
