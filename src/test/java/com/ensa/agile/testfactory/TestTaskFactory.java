package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.task.task.TaskJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestTaskFactory {

    public static TaskJpaEntity validJpaTask( SprintBackLogJpaEntity sb, UserStoryJpaEntity story,UserJpaEntity logedUser) {
        return TaskJpaEntity.builder()
            .title("Implement login feature")
            .description("Create a login page with authentication")
        .estimatedHours(2.0)
        .userStory(story)
        .sprintBackLog(sb)
        .createdBy(logedUser.getId())
        .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<TaskJpaEntity> multipleJpaTasks(SprintBackLogJpaEntity sb, UserStoryJpaEntity story, UserJpaEntity logedUser, Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validJpaTask(sb, story, logedUser))
            .toList();
    }
    
}
