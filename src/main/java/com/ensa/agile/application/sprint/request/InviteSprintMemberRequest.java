package com.ensa.agile.application.sprint.request;

import java.util.UUID;

import com.ensa.agile.application.common.request.InviteRequest;
import com.ensa.agile.domain.global.exception.ValidationException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class InviteSprintMemberRequest extends InviteRequest {
    private UUID sprintId;

    public InviteSprintMemberRequest(UUID productId, UUID sprintId,
                                     String userEmail) {
        super(productId, userEmail);
        this.sprintId = sprintId;
    }

    public InviteSprintMemberRequest(UUID sprintId, InviteRequest req) {
        super(req.getProductId(), req.getEmail());
        this.sprintId = sprintId;
    }

    public void validate() {
        super.validate();
        if (sprintId == null) {
            throw new ValidationException("Sprint ID cannot be null");
        }
    }
}
