package com.ensa.agile.application.sprint.request;

import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class RemoveSprintMemberRequest extends RemoveRequest {

    private String sprintId;
    public RemoveSprintMemberRequest(String productId, String sprintId,
                                     String userEmail) {
        super(productId, userEmail);
        this.sprintId = sprintId;
    }

    public void validate() {
        super.validate();
        if (sprintId == null || sprintId.isBlank()) {
            throw new ValidationException("Sprint ID cannot be null or empty");
        }
    }
}
