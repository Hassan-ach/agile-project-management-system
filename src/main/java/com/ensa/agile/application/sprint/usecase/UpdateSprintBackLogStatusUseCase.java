package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.common.request.UpdateStatusRequest;
import com.ensa.agile.application.common.response.UpdateStatusResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateSprintBackLogStatusUseCase
    extends BaseUseCase<UpdateStatusRequest<SprintStatus>,
                        UpdateStatusResponse<SprintHistory>> {

    private final SprintHistoryRepository sprintHistoryRepository;
    private final SprintBackLogRepository sprintBackLogRepository;

    public UpdateSprintBackLogStatusUseCase(
        ITransactionalWrapper tr,
        SprintHistoryRepository sprintHistoryRepository,
        SprintBackLogRepository sprintBackLogRepository) {
        super(tr);
        this.sprintHistoryRepository = sprintHistoryRepository;
        this.sprintBackLogRepository = sprintBackLogRepository;
    }

    public UpdateStatusResponse<SprintHistory>
    execute(UpdateStatusRequest<SprintStatus> request) {

        SprintBackLog sprint =
            this.sprintBackLogRepository.findById(request.getId());
        SprintHistory status = sprint.getStatus();

        // check if the new status is CANCELLED, if not validate the transition
        if (request.getStatus() != SprintStatus.CANCELLED) {
            if (status.getStatus() == request.getStatus()) {
                return UpdateStatusResponse.<SprintHistory>builder()
                    .status(null)
                    .updated(false)
                    .message("Status is already " + request.getStatus().name())
                    .build();
            }

            SprintStatus nextStatus = status.getNextStatus();

            if (nextStatus != request.getStatus()) {
                return UpdateStatusResponse.<SprintHistory>builder()
                    .status(null)
                    .updated(false)
                    .message("Invalid status transition from " +
                             status.getStatus().name() + " to " +
                             request.getStatus().name())
                    .build();
            }
        }
        SprintHistory newStatus = SprintHistory.builder()
                                      .sprint(sprint)
                                      .status(request.getStatus())
                                      .note(request.getNote())
                                      .build();
        return UpdateStatusResponse.<SprintHistory>builder()
            .status(this.sprintHistoryRepository.save(newStatus))
            .updated(true)
            .message("Sprint status updated to " + request.getStatus().name())
            .build();
    }
}
