package com.ensa.agile.application.sprint.response;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintHistoryResponse {
    private UUID id;
    private SprintStatus status;
    private String note;
}
