package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.common.request.UpdateStatusRequest;
import com.ensa.agile.application.common.response.UpdateStatusResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintHistoryResponseMapper;
import com.ensa.agile.application.sprint.response.SprintHistoryResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateSprintBackLogStatusUseCase
    extends BaseUseCase<UpdateStatusRequest<SprintStatus>,
                        UpdateStatusResponse<SprintHistoryResponse>> {

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

    public UpdateStatusResponse<SprintHistoryResponse>
    execute(UpdateStatusRequest<SprintStatus> request) {

        SprintBackLog sprint =
            this.sprintBackLogRepository.findById(request.getId());
        SprintHistory status = sprint.getStatus();
        status.setSprint(sprint);

        // check if the new status is the current status
        if (status.getStatus() == request.getStatus()) {
            return UpdateStatusResponse.<SprintHistoryResponse>builder()
                .status(null)
                .updated(false)
                .message("Status is already " + request.getStatus().name())
                .build();
        }

        SprintHistory newStatus = this.sprintHistoryRepository.save(
            status.updateState(request.getStatus(), request.getNote()));

        return UpdateStatusResponse.<SprintHistoryResponse>builder()
            .status(SprintHistoryResponseMapper.toResponse(newStatus))
            .updated(true)
            .message("Sprint status updated to " + request.getStatus().name())
            .build();
    }
}
