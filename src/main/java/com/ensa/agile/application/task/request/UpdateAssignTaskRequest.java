package com.ensa.agile.application.task.request;

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
        ValidationUtil.requireValidEmail(assigneeEmail, "Assignee email");
        this.id = id;
        this.assigneeEmail = assigneeEmail;
    }

    public UpdateAssignTaskRequest(UUID id, UpdateAssignTaskRequest req) {
        ValidationUtil.requireNonNull(req, "Update assign task request");
        ValidationUtil.requireNonNull(id, "Task ID");
        this.id = id;
        this.assigneeEmail = req.getAssigneeEmail();
    }
}
