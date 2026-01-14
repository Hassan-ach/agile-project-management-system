package com.ensa.agile.domain.product.entity;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
public class ProductBackLog extends BaseDomainEntity {

    private String name;
    private String description;
    private List<Epic> epics;
    private List<UserStory> userStories;
    private List<ProjectMember> projectMembers;
    private List<SprintBackLog> sprintBackLogs;

    protected ProductBackLog(ProductBackLogBuilder<?, ?> b) {
        super(b);
        this.name = b.name;
        this.description = b.description;
        this.epics = b.epics != null ? b.epics : new ArrayList<>();
        this.userStories =
            b.userStories != null ? b.userStories : new ArrayList<>();
        this.projectMembers =
            b.projectMembers != null ? b.projectMembers : new ArrayList<>();
        this.sprintBackLogs =
            b.sprintBackLogs != null ? b.sprintBackLogs : new ArrayList<>();

        validate();
    }

    public void updateMetadata(String name, String description) {
        if (name != null)
            this.name = name;
        if (description != null)
            this.description = description;

        this.validate();
    }

    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new ValidationException(
                "Product Backlog name cannot be null or empty");
        }

        if (this.description == null || this.description.isEmpty()) {
            throw new ValidationException(
                "Product Backlog description cannot be null or empty");
        }
    }
}
