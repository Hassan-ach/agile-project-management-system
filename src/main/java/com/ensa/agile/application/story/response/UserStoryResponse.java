package com.ensa.agile.application.story.response;

import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.domain.story.enums.StoryStatus;
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
public class UserStoryResponse {
    private UUID id;
    private String title;
    private String description;
    private MoscowType priority;
    private Integer storyPoints;
    private String acceptanceCriteria;
    private StoryStatus status;
    private UserStoryHistoryResponse latestHistory;
    private List<UserStoryHistoryResponse> history;

    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private List<TaskResponse> tasks;
}
