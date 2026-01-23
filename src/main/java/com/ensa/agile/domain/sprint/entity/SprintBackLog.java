package com.ensa.agile.domain.sprint.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.user.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class SprintBackLog extends BaseDomainEntity {

    private String name;
    private ProductBackLog productBackLog;
    private User scrumMaster;
    private List<SprintMember> members;
    private List<UserStory> userStories;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goal;
    private SprintHistory status;
    private List<SprintHistory> sprintHistories;

    protected SprintBackLog(SprintBackLogBuilder<?, ?> b) {
        super(b);
        this.name = b.name != null ? b.name : "";
        this.productBackLog =
            b.productBackLog != null ? b.productBackLog : null;
        this.scrumMaster = b.scrumMaster != null ? b.scrumMaster : null;
        this.members = b.members != null ? b.members : new ArrayList<>();
        this.userStories =
            b.userStories != null ? b.userStories : new ArrayList<>();
        this.startDate = b.startDate;
        this.endDate = b.endDate;
        this.goal = b.goal != null ? b.goal : "";
        this.status = b.status;
        this.sprintHistories =
            b.sprintHistories != null ? b.sprintHistories : new ArrayList<>();
        validate();
    }

    public void updateMetadata(String name, LocalDate startDate,
                               LocalDate endDate, String goal) {
        if (name != null) {
            this.name = name;
        }

        if (startDate != null) {
            this.startDate = startDate;
        }

        if (endDate != null) {
            this.endDate = endDate;
        }

        if (goal != null) {
            this.goal = goal;
        }

        this.validate();
    }

    public void updateScrumMaster(User scrumMaster) {
        this.scrumMaster = scrumMaster;
        this.validate();
    }

    public void validate() {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(
                "Sprint start date must be before end date");
        }

        if (name.isBlank()) {
            throw new ValidationException("Sprint name must not be empty");
        }

        if (goal.isBlank()) {
            throw new ValidationException("Sprint goal must not be empty");
        }
    }
}
