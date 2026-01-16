package com.ensa.agile.application.sprint.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SprintBackLogCreateRequest {
    private final String name;
    private final String scrumMasterEmail;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String goal;
    private final List<UUID> userStoriesIds;
    private UUID productId;

    // This constructor is for validation purposes
    public SprintBackLogCreateRequest(UUID productId,
                                      SprintBackLogCreateRequest req) {
        if (req == null) {
            throw new ValidationException("request cannot be null");
        }
        if (productId == null || !ValidationUtil.isValidUUID(productId)) {
            throw new ValidationException(
                "productId cannot be null or invalid");
        }
        if (req.getName() == null || req.getName().isBlank()) {
            throw new ValidationException("name cannot be null or blank");
        }
        if (req.getScrumMasterEmail() == null ||
            req.getScrumMasterEmail().isBlank()) {
            throw new ValidationException(
                "scrumMasterEmail cannot be null or blank");
        }
        if (!ValidationUtil.isValidEmail(req.getScrumMasterEmail())) {
            throw new ValidationException("scrumMaster Email is not valid");
        }
        if (req.getStartDate() == null) {
            throw new ValidationException("startDate cannot be null");
        }
        if (req.getEndDate() == null) {
            throw new ValidationException("endDate cannot be null");
        }
        if (req.getEndDate().isBefore(req.getStartDate())) {
            throw new ValidationException("endDate cannot be before startDate");
        }

        if (req.getStartDate().isBefore(LocalDate.now())) {
            throw new ValidationException("startDate cannot be in the past");
        }

        if (req.getGoal() == null || req.getGoal().isBlank()) {
            throw new ValidationException("goal cannot be null or blank");
        }
        this.name = req.getName();
        this.scrumMasterEmail = req.getScrumMasterEmail();
        this.startDate = req.getStartDate();
        this.endDate = req.getEndDate();
        this.goal = req.getGoal();
        this.userStoriesIds = req.getUserStoriesIds() != null
                                  ? req.getUserStoriesIds()
                                  : List.of();
        this.productId = productId;
    }
}
