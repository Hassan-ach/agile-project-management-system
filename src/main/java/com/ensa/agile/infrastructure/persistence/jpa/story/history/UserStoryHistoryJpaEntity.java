package com.ensa.agile.infrastructure.persistence.jpa.story.history;

import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "user_story_histories",
       indexes =
       {
           @Index(name = "idx_user_story_history_user_story",
                  columnList = "user_story_id")
       })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserStoryHistoryJpaEntity extends BaseJpaEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_story_id", nullable = false)
    private UserStoryJpaEntity userStory;

    @Enumerated(EnumType.STRING) private StoryStatus status;

    private String note;
}
