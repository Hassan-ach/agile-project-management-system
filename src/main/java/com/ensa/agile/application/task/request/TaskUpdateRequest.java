package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskUpdateRequest {
    private String id;
    private String title;
    private String description;
    private Double estimatedHours;
    private String assigneeEmail;
    private Double actualHours;

    private String userStoryId;
    private String sprintId;

    public TaskUpdateRequest(String sprintId, String userStoryId, String id,
                             TaskUpdateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        if (id == null || id.isBlank()) {
            throw new ValidationException("id cannot be null or blank");
        }
        if (sprintId == null || sprintId.isBlank()) {
            throw new ValidationException("sprintId cannot be null or blank");
        }
        if (userStoryId == null || userStoryId.isBlank()) {
            throw new ValidationException(
                "userStoryId cannot be null or blank");
        }

        if (req.title == null && req.description == null &&
            req.estimatedHours == null && req.assigneeEmail == null &&
            req.getActualHours() == null) {
            throw new ValidationException(
                "At least one field must be provided for update");
        }

        this.id = id;
        this.sprintId = sprintId;
        this.userStoryId = userStoryId;

        if (req.title != null) {
            if (req.title.isBlank()) {
                throw new ValidationException("title cannot be blank");
            } else {
                this.title = req.title;
            }
        }

        if (req.description != null) {
            if (req.description.isBlank()) {
                throw new ValidationException("description cannot be blank");
            } else {
                this.description = req.description;
            }
        }

        if (req.estimatedHours != null && req.estimatedHours > 0) {
            this.estimatedHours = req.estimatedHours;
        }
        if (req.actualHours != null && req.actualHours >= 0) {
            this.actualHours = req.actualHours;
        }

        if (req.assigneeEmail != null) {
            if (req.assigneeEmail.isBlank() ||
                !ValidationUtil.isValidEmail(req.assigneeEmail)) {
                throw new ValidationException("assigneeEmail is not valid");
            } else {
                this.assigneeEmail = req.assigneeEmail;
            }
        }
    }
}
