package com.ensa.agile.application.story.request;

import java.util.UUID;

import com.ensa.agile.domain.global.exception.ValidationException;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class UserStoryEpicUpdateRequest {
    private UUID id;
    private UUID epicId;

    public UserStoryEpicUpdateRequest(UUID id, UUID epicId) {
        this.id = id;
        this.epicId = epicId;
        validate();
    }

    public void validate() {
        if (id == null) {
            throw new ValidationException("Story ID cannot be null");
        }
        if (epicId == null) {
            throw new ValidationException("Epic ID cannot be null");
        }
    }
    
}
