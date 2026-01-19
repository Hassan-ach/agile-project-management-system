package com.ensa.agile.application.product.response;

import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.response.UserStoryResponse;
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
public class ProductBackLogResponse {

    private UUID id;
    private String name;
    private String description;
    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private List<ProjectMemberResponse> projectMembers;
    private List<SprintBackLogResponse> sprintBackLogs;
    private List<EpicResponse> epics;
    private List<UserStoryResponse> userStories;
}
