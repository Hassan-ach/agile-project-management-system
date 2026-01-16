package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssignTaskRequest {
    private final UUID id;
    private final UUID productId;
    private final UUID sprintId;
    private final UUID userStoryId;
    private final String assigneeEmail;

    public AssignTaskRequest(UUID id, UUID productId, UUID sprintId,
                             UUID userStoryId, String assigneeEmail) {
        if (!ValidationUtil.isValidEmail(assigneeEmail)) {
            throw new ValidationException("Invalid email format");
        }

        if (id == null || productId == null || sprintId == null ||
            userStoryId == null) {
            throw new ValidationException("IDs cannot be null");
        }

        this.productId = productId;
        this.sprintId = sprintId;
        this.userStoryId = userStoryId;
        this.id = id;
        this.assigneeEmail = assigneeEmail;
    }
}
