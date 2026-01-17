package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.infrastructure.persistence.jpa.story.history.UserStoryHistoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestUserStoryHistoryFactory {

    public static UserStoryHistoryJpaEntity validUserStoryHistoryJpaEntity(UserStoryJpaEntity userStory, StoryStatus status, UserJpaEntity logedUser) {
        return UserStoryHistoryJpaEntity.builder()
        .userStory(userStory)
.status(status)
            .note("Valid User Story History Note")
        .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<UserStoryHistoryJpaEntity> multipleUserStoryHistoryJpaEntities(UserStoryJpaEntity userStory, StoryStatus status, UserJpaEntity logedUser, Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validUserStoryHistoryJpaEntity(userStory, status, logedUser))
            .toList();
    }
    
}
