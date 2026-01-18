package com.ensa.agile.application.sprint.mapper;

import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.ArrayList;
import java.util.List;

public class SprintBackLogResponseMapper {

    public static SprintBackLogResponse toResponse(SprintBackLog sprint,
                                                   List<UserStory> userStories,
                                                   SprintHistory status) {
        return SprintBackLogResponse.builder()
            .id(sprint.getId())
            .name(sprint.getName())
            .status(sprint.getStatus())
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .userStories(userStories.stream()
                             .map(UserStoryResponseMapper::toResponse)
                             .toList())
            .status(status)
            .createdBy(sprint.getCreatedBy())
            .createdDate(sprint.getCreatedDate())
            .lastModifiedBy(sprint.getLastModifiedBy())
            .lastModifiedDate(sprint.getLastModifiedDate())
            .members(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }
    public static SprintBackLogResponse
    toResponse(SprintBackLog sprint, List<UserStory> userStories) {
        return SprintBackLogResponse.builder()
            .id(sprint.getId())
            .name(sprint.getName())
            .status(sprint.getStatus())
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .userStories(userStories.stream()
                             .map(UserStoryResponseMapper::toResponse)
                             .toList())
            .createdBy(sprint.getCreatedBy())
            .createdDate(sprint.getCreatedDate())
            .lastModifiedBy(sprint.getLastModifiedBy())
            .lastModifiedDate(sprint.getLastModifiedDate())
            .members(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }

    public static SprintBackLogResponse toResponse(SprintBackLog sprint) {
        return SprintBackLogResponse.builder()
            .id(sprint.getId())
            .name(sprint.getName())
            .status(sprint.getStatus())
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .createdBy(sprint.getCreatedBy())
            .createdDate(sprint.getCreatedDate())
            .lastModifiedBy(sprint.getLastModifiedBy())
            .lastModifiedDate(sprint.getLastModifiedDate())
            .members(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }
    public static SprintBackLogResponse toResponse(SprintBackLog sprint,
                                                   SprintHistory status) {
        return SprintBackLogResponse.builder()
            .id(sprint.getId())
            .name(sprint.getName())
            .status(sprint.getStatus())
            .startDate(sprint.getStartDate())
            .endDate(sprint.getEndDate())
            .status(status)
            .createdBy(sprint.getCreatedBy())
            .createdDate(sprint.getCreatedDate())
            .lastModifiedBy(sprint.getLastModifiedBy())
            .lastModifiedDate(sprint.getLastModifiedDate())
            .members(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }
}
