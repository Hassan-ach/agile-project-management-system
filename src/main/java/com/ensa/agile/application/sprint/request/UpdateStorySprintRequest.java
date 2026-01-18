package com.ensa.agile.application.sprint.request;

import java.util.UUID;

import com.ensa.agile.domain.global.exception.ValidationException;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class UpdateStorySprintRequest {

    private UUID id;
    private UUID storyId;

    public UpdateStorySprintRequest(UUID id, UUID storyId) {
        this.id = id;
        this.storyId = storyId;

        validate();
    }
    
    public void validate() {
        if (storyId == null) {
            throw new ValidationException("Story ID cannot be null");
        }
        if (id == null) {
            throw new ValidationException("Sprint ID cannot be null");
        }
    }
}
