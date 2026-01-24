package com.ensa.agile.application.sprint.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintBackLogUpdateRequest {
    private UUID id;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String scrumMasterEmail;

    public SprintBackLogUpdateRequest(UUID id, SprintBackLogUpdateRequest req) {

        ValidationUtil.requireNonNull(req, "request");

        ValidationUtil.requireNonNull(id, "sprint id");

        if (req.getName() == null && req.getGoal() == null &&
            req.getStartDate() == null && req.getEndDate() == null &&
            req.getScrumMasterEmail() == null) {
            throw new ValidationException(
                "At least one field must be provided for update");
        }
        this.name = ValidationUtil.update(this.name, req.getName(),
                                          ValidationUtil::requireNonBlank,
                                          "sprint name");

        this.goal = ValidationUtil.update(this.goal, req.getGoal(),
                                          ValidationUtil::requireNonBlank,
                                          "sprint goal");

        this.startDate = ValidationUtil.update(
            this.startDate, req.getStartDate(),
            ValidationUtil::requireFutureDate, "sprint start date");

        this.endDate = ValidationUtil.update(this.endDate, req.getEndDate(),
                                             ValidationUtil::requireFutureDate,
                                             "sprint end date");

        this.scrumMasterEmail = ValidationUtil.update(
            this.scrumMasterEmail, req.getScrumMasterEmail(),
            ValidationUtil::requireValidEmail, "scrum master email");

        this.id = id;
    }
}
