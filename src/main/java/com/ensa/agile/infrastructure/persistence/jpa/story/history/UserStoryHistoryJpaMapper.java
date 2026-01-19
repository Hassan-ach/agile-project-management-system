package com.ensa.agile.infrastructure.persistence.jpa.story.history;

import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaMapper;

public class UserStoryHistoryJpaMapper {
    public static UserStoryHistoryJpaEntity
    toJpaEntity(UserStoryHistory domain) {
        if (domain == null) {
            return null;
        }
        return UserStoryHistoryJpaEntity.builder()
            .id(domain.getId())
            .userStory(domain.getUserStory() == null ? null :
            UserStoryJpaMapper.toJpaEntity(domain.getUserStory()))
            .status(domain.getStatus())
            .note(domain.getNote())
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    public static UserStoryHistory
    toDomainEntity(UserStoryHistoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return UserStoryHistory.builder()
            .id(jpaEntity.getId())
            .userStory(jpaEntity.getUserStory() == null ? null :
                UserStoryJpaMapper.toDomainEntity(jpaEntity.getUserStory()))
            .status(jpaEntity.getStatus())
            .note(jpaEntity.getNote())
            .createdBy(jpaEntity.getCreatedBy())
            .createdDate(jpaEntity.getCreatedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .build();
    }
}
