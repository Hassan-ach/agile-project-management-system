package com.ensa.agile.application.task.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AssignTaskResponse {
    private TaskResponse task;
    private boolean assigned;
    private String message;
}
