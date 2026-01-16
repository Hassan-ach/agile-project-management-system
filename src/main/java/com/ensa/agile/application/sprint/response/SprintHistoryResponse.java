package com.ensa.agile.application.sprint.response;

import java.util.UUID;

import com.ensa.agile.domain.sprint.enums.SprintStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintHistoryResponse {
    private UUID statusId;
    private SprintStatus status;
    private String note;
}
