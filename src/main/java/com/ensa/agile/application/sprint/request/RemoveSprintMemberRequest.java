package com.ensa.agile.application.sprint.request;

import com.ensa.agile.application.common.request.RemoveRequest;
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
                                     UUID userId) {
        super(productId, userId);
        this.sprintId = sprintId;
    }
}
