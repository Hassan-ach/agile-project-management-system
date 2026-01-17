package com.ensa.agile.testfactory;

import java.time.LocalDateTime;

import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestUserStoryFactory {

    public static UserStory validUserStory() {
        return UserStory.builder()
            .title("Valid User Story Title")
            .description("Valid User Story Description")
            .priority(MoscowType.MUST_HAVE)
            .storyPoints(5)
            .acceptanceCriteria("Valid Acceptance Criteria")
            .productBackLog(TestProductBackLogFactory.validProduct())
            .build();
    }

    public static UserStoryJpaEntity validJpaUserStory(ProductBackLogJpaEntity pb, UserJpaEntity logedUser){
        return validJpaUserStoryWithEpic(pb, null, logedUser);
    }

    public static UserStoryJpaEntity validJpaUserStoryWithEpic(ProductBackLogJpaEntity pb,EpicJpaEntity epic, UserJpaEntity logedUser){
//
        return UserStoryJpaEntity.builder()
            .title("Valid User Story Title")
            .description("Valid User Story Description")
            .priority(MoscowType.MUST_HAVE)
            .storyPoints(5)
            .acceptanceCriteria("Valid Acceptance Criteria")
            .productBackLog(pb)
            .epic(epic)
            .createdBy(logedUser.getId())
            .createdDate(LocalDateTime.now())
            .build();
    }
}
