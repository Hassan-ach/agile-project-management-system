package com.ensa.agile.infrastructure.persistence.jpa.story.userstory;

import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.story.history.UserStoryHistoryJpaMapper;

public class UserStoryJpaMapper {

    public static UserStoryJpaEntity toJpaEntity(UserStory entity) {
        if (entity == null) {
            return null;
        }
        var us = UserStoryJpaEntity.builder()
                     .id(entity.getId())
                     .title(entity.getTitle())
                     .description(entity.getDescription())
                     .priority(entity.getPriority())
                     .storyPoints(entity.getStoryPoints())
                     .acceptanceCriteria(entity.getAcceptanceCriteria())
                     .productBackLog(entity.getProductBackLog() == null? null : ProductBackLogJpaMapper.toJpaEntity(
                         entity.getProductBackLog()))
                     .createdDate(entity.getCreatedDate())
                     .createdBy(entity.getCreatedBy())
                     .lastModifiedDate(entity.getLastModifiedDate())
                     .lastModifiedBy(entity.getLastModifiedBy())
                     .build();

        if (entity.getEpic() != null) {
            us.setEpic(EpicJpaMapper.toJpaEntity(entity.getEpic()));
        }
        if (entity.getSprintBackLog() != null) {
            us.setSprintBackLog(
                SprintBackLogJpaMapper.toJpaEntity(entity.getSprintBackLog()));
        }
        return us;
    }

    public static UserStory toDomainEntity(UserStoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return UserStory.builder()
            .id(jpaEntity.getId())
            .title(jpaEntity.getTitle())
            .description(jpaEntity.getDescription())
            .priority(jpaEntity.getPriority())
            .storyPoints(jpaEntity.getStoryPoints())
            .acceptanceCriteria(jpaEntity.getAcceptanceCriteria())
            .status( jpaEntity.getStatus() == null ? null :
                UserStoryHistoryJpaMapper.toDomainEntity(jpaEntity.getStatus()))
            .productBackLog( jpaEntity.getProductBackLog() == null ? null :
            ProductBackLogJpaMapper.toDomainEntity(
                jpaEntity.getProductBackLog()))
            .epic(jpaEntity.getEpic() != null
                      ? EpicJpaMapper.toDomainEntity(jpaEntity.getEpic())
                      : null)
            .sprintBackLog(jpaEntity.getSprintBackLog() != null
                               ? SprintBackLogJpaMapper.toDomainEntity(
                                     jpaEntity.getSprintBackLog())
                               : null)
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }

    public static UserStory toDomainEntityPartial(
        UserStoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return UserStory.builder()
            .id(jpaEntity.getId())
            .title(jpaEntity.getTitle())
            .description(jpaEntity.getDescription())
            .priority(jpaEntity.getPriority())
            .storyPoints(jpaEntity.getStoryPoints())
            .acceptanceCriteria(jpaEntity.getAcceptanceCriteria())
            .status( jpaEntity.getStatus() == null ? null :
                UserStoryHistoryJpaMapper.toDomainEntity(jpaEntity.getStatus()))
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }
}
