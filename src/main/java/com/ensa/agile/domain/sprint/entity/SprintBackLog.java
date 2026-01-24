package com.ensa.agile.domain.sprint.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
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
        this.name = b.name;
        this.productBackLog = b.productBackLog;
        this.scrumMaster = b.scrumMaster;
        this.members = b.members != null ? b.members : new ArrayList<>();
        this.userStories =
            b.userStories != null ? b.userStories : new ArrayList<>();
        this.startDate = b.startDate;
        this.endDate = b.endDate;
        this.goal = b.goal;
        this.status = b.status;
        this.sprintHistories =
            b.sprintHistories != null ? b.sprintHistories : new ArrayList<>();
        validate();
    }

    public void updateMetadata(String name, LocalDate startDate,
                               LocalDate endDate, String goal) {
        this.name = ValidationUtil.update(
            this.name, name, ValidationUtil::requireNonBlank, "sprint name");

        this.startDate = ValidationUtil.update(
            this.startDate, startDate, ValidationUtil::requireFutureDate,
            "sprint start date");

        this.endDate = ValidationUtil.update(this.endDate, endDate,
                                             ValidationUtil::requireFutureDate,
                                             "sprint end date");

        ValidationUtil.requireBefore(this.startDate, "sprint start date",
                                     endDate, "sprint end date");

        this.goal = ValidationUtil.update(
            this.goal, goal, ValidationUtil::requireNonBlank, "sprint goal");
    }

    public void updateScrumMaster(User scrumMaster) {
        this.scrumMaster = scrumMaster;
        this.validate();
    }

    public void validate() {
        ValidationUtil.requireBefore(this.startDate, "sprint start date",
                                     endDate, "sprint end date");
        ValidationUtil.requireNonBlank(goal, "sprint goal");
    }
}
