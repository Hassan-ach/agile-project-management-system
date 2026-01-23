package com.ensa.agile.application.task.response;

import com.ensa.agile.domain.task.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskHistoryResponse {
    private UUID id;
    private TaskStatus status;
    private String note;

    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
