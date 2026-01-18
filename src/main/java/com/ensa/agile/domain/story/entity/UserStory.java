package com.ensa.agile.domain.story.entity;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.domain.task.entity.Task;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class UserStory extends BaseDomainEntity {

    private String title;
    private String description;
    private MoscowType priority;
    private Integer storyPoints;
    private String acceptanceCriteria;
    private Epic epic;
    private ProductBackLog productBackLog;
    private SprintBackLog sprintBackLog;
    private List<Task> tasks;
    private List<UserStoryHistory> userStoryHistories;
    private UserStoryHistory status;

    protected UserStory(UserStoryBuilder<?, ?> b) {
        super(b);
        this.title = b.title;
        this.description = b.description;
        this.priority = b.priority;
        this.storyPoints = b.storyPoints;
        this.acceptanceCriteria = b.acceptanceCriteria;
        this.epic = b.epic;
        this.productBackLog = b.productBackLog;
        this.sprintBackLog = b.sprintBackLog;
        this.tasks = b.tasks != null ? b.tasks : new ArrayList<>();
        this.userStoryHistories = b.userStoryHistories != null
                                      ? b.userStoryHistories
                                      : new ArrayList<>();
        this.status = b.status;

        validate();
    }

    public void unlinkEpic() {
        this.epic = null;
    }
    public void linkEpic(Epic epic) {
        this.epic = epic;
    }


    public void unlinkSprint() {
        this.sprintBackLog = null;
    }
    public void linkSprint(SprintBackLog sprintBackLog) {
        this.sprintBackLog = sprintBackLog;
    }
    public void updatePriority(MoscowType priority) {
        this.priority = priority;
    }

    public void makeMustHave() {
        this.priority = MoscowType.MUST_HAVE;
    }
    public void makeShouldHave() {
        this.priority = MoscowType.SHOULD_HAVE;
    }
    public void makeCouldHave() {
        this.priority = MoscowType.COULD_HAVE;
    }
    public void makeWontHave() {
        this.priority = MoscowType.WONT_HAVE;
    }


    public void validate() {
        if (title == null || title.isEmpty() || title.isBlank()) {
            throw new ValidationException("Title cannot be null or empty");
        }
        if (description == null || description.isEmpty() ||
            description.isBlank()) {
            throw new ValidationException(
                "Description cannot be null or empty");
        }
        if (priority == null) {
            throw new ValidationException("Priority cannot be null");
        }
        if (storyPoints == null || storyPoints < 0 || storyPoints > 100) {
            throw new ValidationException(
                "Story points cannot be null or negative");
        }
        if (acceptanceCriteria == null || acceptanceCriteria.isEmpty() ||
            acceptanceCriteria.isBlank()) {
            throw new ValidationException(
                "Acceptance criteria cannot be null or empty");
        }

        if (productBackLog == null) {
            throw new ValidationException(
                "User story must be associated with a product backlog.");
        }
    }

    public void updateMetaData(String title, String description,
                               MoscowType priority, Integer storyPoints,
                               String acceptanceCriteria) {
        if (title != null)
            this.title = title;
        if (description != null)
            this.description = description;
        if (priority != null)
            this.priority = priority;
        if (storyPoints != null)
            this.storyPoints = storyPoints;
        if (acceptanceCriteria != null)
            this.acceptanceCriteria = acceptanceCriteria;

        validate();
    }
}
