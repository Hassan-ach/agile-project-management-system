package com.ensa.agile.application.epic.mapper;

import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.domain.epic.entity.Epic;

public class EpicResponseMapper {

    public static EpicResponse toResponse(Epic epic) {
        if (epic == null) {
            return new EpicResponse();
        }
        return EpicResponse.builder()
            .id(epic.getId())
            .title(epic.getTitle())
            .description(epic.getDescription())
            .build();
    }

    public static EpicResponse toResponseWithUserStories(Epic epic) {
        if (epic == null) {
            return new EpicResponse();
        }
        return EpicResponse.builder()
            .id(epic.getId())
            .title(epic.getTitle())
            .description(epic.getDescription())
            .userStories(epic.getUserStories()
                             .stream()
                             .map(UserStoryResponseMapper::toResponse)
                             .toList())
            .build();
    }
}
