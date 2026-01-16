package com.ensa.agile.application.sprint.request;

import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class RemoveSprintMemberRequest extends RemoveRequest {

    private UUID sprintId;
    public RemoveSprintMemberRequest(UUID productId, UUID sprintId,
                                     String userEmail) {
        super(productId, userEmail);
        this.sprintId = sprintId;
    }

    public void validate() {
        super.validate();
        if (sprintId == null || !ValidationUtil.isValidUUID(sprintId)) {
            throw new ValidationException("Sprint ID cannot be null or empty");
        }
    }
}
