package com.ensa.agile.domain.story.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.story.enums.StoryStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserStoryHistory extends BaseDomainEntity {

    private final UserStory userStory;
    private final StoryStatus status;
    private final String note;

    public void validate() {
        if (this.userStory == null) {
            throw new ValidationException(
                "UserStoryHistory must be associated with a UserStory.");
        }
        if (this.status == null) {
            throw new ValidationException(
                "UserStoryHistory must have a valid status.");
        }
        if (this.note != null && this.note.length() > 500) {
            throw new ValidationException(
                "UserStoryHistory note cannot exceed 500 characters.");
        }
    }

    public StoryStatus getNextStatus() {

        switch (this.status) {
        case TODO:
            return StoryStatus.IN_PROGRESS;
        case IN_PROGRESS:
            return StoryStatus.DONE;
        case DONE:
            throw new ValidationException(
                "Cannot transition from DONE to another status.");
        default:
            throw new ValidationException("Invalid status transition.");
        }
    }
}
