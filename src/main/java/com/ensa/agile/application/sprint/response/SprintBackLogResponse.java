package com.ensa.agile.application.sprint.response;

import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import java.time.LocalDate;
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
public class SprintBackLogResponse {
    private UUID id;
    private String name;
    private String scrumMasterEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goal;
    private SprintHistoryResponse latestHistory;
    private SprintStatus status;
    private List<SprintHistoryResponse> sprintHistories;

    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private List<SprintMemberResponse> members;
    private List<UserStoryResponse> userStories;
}
