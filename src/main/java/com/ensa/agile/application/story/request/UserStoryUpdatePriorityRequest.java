package com.ensa.agile.application.story.request;

import java.util.UUID;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.story.enums.MoscowType;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class UserStoryUpdatePriorityRequest {

    private UUID id;
    private MoscowType priority;

    public UserStoryUpdatePriorityRequest(UUID id, MoscowType priority) {
        this.id = id;
        this.priority = priority;
        validate();
    }
    public UserStoryUpdatePriorityRequest(UUID id, UserStoryUpdatePriorityRequest request) {
        this.id = id;
        this.priority = request.getPriority();
        validate();
    }

    public void validate() {
        if (id == null) {
            throw new ValidationException("Story ID cannot be null");
        }
        if (priority == null) {
            throw new ValidationException("Epic ID cannot be null");
        }
    }
    
}
