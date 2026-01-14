package com.ensa.agile.application.sprint.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintBackLogUpdateRequest {
    private String id;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String scrumMasterEmail;

    private SprintStatus status;

    public SprintBackLogUpdateRequest(String id,
                                      SprintBackLogUpdateRequest req) {

        if (req == null) {
            throw new ValidationException("request cannot be null");
        }

        if (id == null || id.isBlank()) {
            throw new ValidationException("ID cannot be null or blank");
        }

        if (req.getName() == null && req.getGoal() == null &&
            req.getStartDate() == null && req.getEndDate() == null &&
            req.getScrumMasterEmail() == null) {
            throw new ValidationException(
                "At least one field must be provided for update");
        }

        if (req.getName() != null) {
            if (req.getName().isBlank()) {
                throw new ValidationException("name cannot be blank");
            } else {
                this.name = req.getName();
            }
        }

        if (req.getGoal() != null) {
            if (req.getGoal().isBlank()) {
                throw new ValidationException("description cannot be blank");
            } else {
                this.goal = req.getGoal();
            }
        }

        if (req.getStartDate() != null) {
            this.startDate = req.getStartDate();
        }

        if (req.getEndDate() != null) {
            this.endDate = req.getEndDate();
        }

        if (req.getScrumMasterEmail() != null) {
            if (req.getScrumMasterEmail().isBlank()) {
                throw new ValidationException(
                    "scrumMasterEmail cannot be blank");
            }
            if (!ValidationUtil.isValidEmail(req.getScrumMasterEmail())) {
                throw new ValidationException("scrumMaster Email is not valid");
            } else {
                this.scrumMasterEmail = req.getScrumMasterEmail();
            }
        }

        this.id = id;
        this.status = req.getStatus();
    }
}
