package com.ensa.agile.application.sprint.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintBackLogCreateRequest {
    private String name;
    private String scrumMasterEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goal;
    private List<UUID> userStoriesIds;
    private UUID productId;

    // This constructor is for validation purposes
    public SprintBackLogCreateRequest(UUID productId,
                                      SprintBackLogCreateRequest req) {
        ValidationUtil.requireNonNull(req, "request");
        ValidationUtil.requireNonNull(productId, "product id");

        ValidationUtil.requireNonBlank(req.getName(), "sprint name");

        ValidationUtil.requireValidEmail(req.getScrumMasterEmail(),
                                         "scrumMaster Email");

        ValidationUtil.requireFutureDate(startDate, "sprint start date");
        ValidationUtil.requireFutureDate(endDate, "sprint end date");

        ValidationUtil.requireBefore(startDate, "sprint start date", endDate,
                                     "sprint end date");

        ValidationUtil.requireNonBlank(req.getGoal(), "sprint goal");

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
