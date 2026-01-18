package com.ensa.agile.domain.task.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class Task extends BaseDomainEntity {

    private String title;
    private String description;
    private UserStory userStory;
    private SprintBackLog sprintBackLog;
    private User assignee;
    private Double estimatedHours;
    private Double actualHours;
    private List<TaskHistory> taskHistories;
    private TaskHistory status;

    protected Task(TaskBuilder<?, ?> b) {
        super(b);
        this.title = b.title;
        this.description = b.description;
        this.userStory = b.userStory;
        this.sprintBackLog = b.sprintBackLog;
        this.assignee = b.assignee;
        this.estimatedHours = b.estimatedHours;
        this.actualHours = b.actualHours;
        this.taskHistories =
            b.taskHistories != null ? b.taskHistories : new ArrayList<>();
        this.status = b.status;
        validate();
    }

    public void assignUser(User user) {
        this.assignee = user;
    }

    public void unassignUser() {
        this.assignee = null;
    }

    public void validate() {
        if (title == null || title.isEmpty()) {
            throw new ValidationException("Title cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new ValidationException(
                "Description cannot be null or empty");
        }
        if (estimatedHours != null && estimatedHours < 0) {
            throw new ValidationException("Estimated hours cannot be negative");
        }
        if (actualHours != null && actualHours < 0) {
            throw new ValidationException("Actual hours cannot be negative");
        }
        if (userStory == null) {
            throw new ValidationException("User Story cannot be null");
        }
        if (sprintBackLog == null) {
            throw new ValidationException("Sprint Backlog cannot be null");
        }
    }
    public void updateMetadata(String title, String description,
                               Double estimatedHours, Double actualHours) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (estimatedHours != null) {
            this.estimatedHours = estimatedHours;
        }
        if (actualHours != null) {
            this.actualHours = actualHours;
        }
        validate();
    }
}
