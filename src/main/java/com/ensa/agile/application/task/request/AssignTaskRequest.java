package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssignTaskRequest {
    private final String id;
    private final String assigneeEmail;
    private final String productId;
    private final String sprintId;
    private final String userStoryId;

    public AssignTaskRequest(String productId, String sprintId,
                             String userStoryId, String id,
                             String assigneeEmail) {
        if (!ValidationUtil.isValidEmail(assigneeEmail)) {
            throw new ValidationException("Invalid email format");
        }

        if (id == null || id.isBlank() || productId == null ||
            productId.isBlank() || sprintId == null || sprintId.isBlank() ||
            userStoryId == null || userStoryId.isBlank()) {
            throw new ValidationException("IDs cannot be null or blank");
        }

        this.productId = productId;
        this.sprintId = sprintId;
        this.userStoryId = userStoryId;
        this.id = id;
        this.assigneeEmail = assigneeEmail;
    }
}
