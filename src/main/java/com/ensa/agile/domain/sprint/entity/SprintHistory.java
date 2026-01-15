package com.ensa.agile.domain.sprint.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SprintHistory extends BaseDomainEntity {

    private final SprintBackLog sprint;
    private final SprintStatus status;
    private final String note;

    protected SprintHistory(SprintHistoryBuilder<?, ?> b) {
        super(b);
        this.sprint = b.sprint;
        this.status = b.status;
        this.note = b.note;

        validate();
    }

    public void validate() {
        if (this.sprint == null) {
            throw new ValidationException(
                "SprintHistory must be associated with a SprintBackLog.");
        }

        if (this.status == null) {
            throw new ValidationException(
                "SprintHistory must have a valid status.");
        }

        if (this.note != null && this.note.length() > 500) {
            throw new ValidationException(
                "SprintHistory note cannot exceed 500 characters.");
        }
    }

    public SprintStatus getNextStatus() {
        switch (this.status) {
        case PLANNED:
            return SprintStatus.ACTIVE;
        case ACTIVE:
            return SprintStatus.COMPLETED;
        case COMPLETED:
            throw new ValidationException(
                "Cannot transition from COMPLETED to another status.");
        case CANCELLED:
            throw new ValidationException(
                "Cannot transition from CANCELLED to another status.");
        default:
            throw new ValidationException("Invalid status transition.");
        }
    }
}
