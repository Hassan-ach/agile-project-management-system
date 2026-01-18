package com.ensa.agile.application.task.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateAssignTaskRequest {
    private final UUID id;
    private final String assigneeEmail;

    public UpdateAssignTaskRequest(UUID id, String assigneeEmail) {
        if (!ValidationUtil.isValidEmail(assigneeEmail)) {
            throw new ValidationException("Invalid email format");
        }

        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }

        this.id = id;
        this.assigneeEmail = assigneeEmail;
    }
}
