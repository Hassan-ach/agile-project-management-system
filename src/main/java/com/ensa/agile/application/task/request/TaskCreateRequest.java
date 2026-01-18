package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskCreateRequest {
    private String title;
    private String description;
    private Double estimatedHours;
    private String assigneeEmail;

    private UUID userStoryId;

    public TaskCreateRequest(UUID userStoryId,
                             TaskCreateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        if (userStoryId == null ) {
            throw new ValidationException(
                "userStoryId cannot be null");
        }
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ValidationException("title cannot be null or blank");
        }

        if (req.getDescription() == null || req.getDescription().isBlank()) {
            throw new ValidationException(
                "description cannot be null or blank");
        }

        if (req.getEstimatedHours() == null || req.getEstimatedHours() <= 0) {
            throw new ValidationException(
                "estimatedHours must be greater than zero");
        }

        if (req.getAssigneeEmail() == null ||
            !ValidationUtil.isValidEmail(req.getAssigneeEmail())) {
            throw new ValidationException("Invalid assignee email format");
        }

        this.title = req.getTitle();
        this.description = req.getDescription();
        this.estimatedHours = req.getEstimatedHours();
        this.assigneeEmail = req.getAssigneeEmail();
        this.userStoryId = userStoryId;
    }
}
