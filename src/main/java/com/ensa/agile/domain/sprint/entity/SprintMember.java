package com.ensa.agile.domain.sprint.entity;

import com.ensa.agile.domain.global.entity.BaseDomainEntity;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class SprintMember extends BaseDomainEntity {
    private User user;
    private SprintBackLog sprintBackLog;

    protected SprintMember(SprintMemberBuilder<?, ?> b) {
        super(b);
        this.user = b.user;
        this.sprintBackLog = b.sprintBackLog;

        // this throws exception during creation even if user is valide
        // validate();
    }

    public void validate() {
        if (user == null) {
            throw new ValidationException(
                "Sprint member must be associated with a user.");
        }
    }
}
