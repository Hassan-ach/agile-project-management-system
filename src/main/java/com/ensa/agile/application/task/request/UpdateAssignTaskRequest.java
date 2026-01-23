package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class UpdateAssignTaskRequest {
    private UUID id;
    private String assigneeEmail;

    public UpdateAssignTaskRequest(UUID id, String assigneeEmail) {
        if (!ValidationUtil.isValidEmail(assigneeEmail)) {
            throw new ValidationException("Invalid email format");
        }
        this.id = id;
        this.assigneeEmail = assigneeEmail;
    }

    public UpdateAssignTaskRequest(UUID id, UpdateAssignTaskRequest req) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (req == null) {
            throw new ValidationException("Request cannot be null");
        }
        this.id = id;
        this.assigneeEmail = req.getAssigneeEmail();
    }
}
