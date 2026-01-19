package com.ensa.agile.application.story.mapper;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.entity.UserStoryHistory;

public class UserStoryResponseMapper {

    public static UserStoryResponse toResponse(UserStory userStory) {
        if (userStory == null) {
            return new UserStoryResponse();
        }
        return UserStoryResponse.builder()
            .id(userStory.getId())
            .title(userStory.getTitle())
            .description(userStory.getDescription())
            .status(userStory.getStatus())
            .priority(userStory.getPriority())
            .storyPoints(userStory.getStoryPoints())
            .acceptanceCriteria(userStory.getAcceptanceCriteria())
            .build();
    }

    public static UserStoryResponse toResponse(UserStory userStory,
                                               UserStoryHistory status) {
        if (userStory == null) {
            return new UserStoryResponse();
        }
        return UserStoryResponse.builder()
            .id(userStory.getId())
            .title(userStory.getTitle())
            .description(userStory.getDescription())
            .status(status)
            .priority(userStory.getPriority())
            .storyPoints(userStory.getStoryPoints())
            .acceptanceCriteria(userStory.getAcceptanceCriteria())
            .build();
    }
}
