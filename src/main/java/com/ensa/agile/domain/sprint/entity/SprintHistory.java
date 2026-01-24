package com.ensa.agile.domain.sprint.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class SprintHistory extends BaseDomainEntity {

    private SprintBackLog sprint;
    private SprintStatus status;
    private String note;

    protected SprintHistory(SprintHistoryBuilder<?, ?> b) {
        super(b);
        this.sprint = b.sprint;
        this.status = b.status;
        this.note = b.note;

        validate();
    }

    public void validate() {
        ValidationUtil.requireNonNull(sprint, "sprint history sprint");

        ValidationUtil.requireMaxLength(note, "sprint history note", 500);
    }

    public SprintHistory updateState(SprintStatus newStatus, String n) {
        var st = ValidationUtil.requireStateTransition(
            this.status, newStatus, "sprint status", (s) -> {
                switch (newStatus) {
                case ACTIVE:
                    return s == SprintStatus.PLANNED;
                case COMPLETED:
                    return s == SprintStatus.ACTIVE;
                case CANCELLED:
                    return s == SprintStatus.PLANNED ||
                        s == SprintStatus.ACTIVE;
                default:
                    return false;
                }
            });

        return SprintHistory.builder()
            .sprint(this.sprint)
            .status(st)
            .note(n)
            .build();
    }
}
