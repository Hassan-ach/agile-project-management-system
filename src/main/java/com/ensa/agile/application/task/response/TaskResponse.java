package com.ensa.agile.application.task.response;

import com.ensa.agile.domain.task.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private String assignee;
    private Double estimatedHours;
    private TaskStatus status;
    private TaskHistoryResponse latestHistory;
    private List<TaskHistoryResponse> history;
    private Double actualHours;

    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
