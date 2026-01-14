package com.ensa.agile.application.product.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.response.UserStoryResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ProductBackLogResponse {

    private final String id;
    private final String name;
    private final String description;
    private final String createdBy;
    private final LocalDateTime createdDate;
    private final String lastModifiedBy;
    private final LocalDateTime lastModifiedDate;

    private List<ProjectMemberResponse> projectMembers;
    private List<SprintBackLogResponse> sprintBackLogs;
    private List<EpicResponse> epics;
    private List<UserStoryResponse> userStories;
}
