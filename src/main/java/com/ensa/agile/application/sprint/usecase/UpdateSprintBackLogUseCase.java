package com.ensa.agile.application.sprint.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintBacklogResponseMapper;
import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import com.ensa.agile.domain.user.repository.UserRepository;

@Component
public class UpdateSprintBackLogUseCase
    extends BaseUseCase<SprintBackLogUpdateRequest, SprintBackLogResponse> {

    private SprintBackLogRepository sprintBackLogRepository;
    private UserRepository userRepository;
    private SprintHistoryRepository sprintHistoryRepository;

    public UpdateSprintBackLogUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository,
        SprintHistoryRepository sprintHistoryRepository,
        UserRepository userRepository) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.sprintHistoryRepository = sprintHistoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SprintBackLogResponse execute(SprintBackLogUpdateRequest request) {
        SprintBackLog sp = sprintBackLogRepository.findById(request.getId());

        sp.updateMetadata(request.getName(), request.getStartDate(),
                          request.getEndDate(), request.getGoal());

        if (request.getScrumMasterEmail() != null) {
            var scrumMaster =
                userRepository.findByEmail(request.getScrumMasterEmail());

            sp.updateScrumMaster(scrumMaster);
        }


        // here i need to check if status can go from current status to the new
        // status
        SprintHistory status = null;
        if (request.getStatus() != null) {
            status = this.sprintHistoryRepository.save(
                SprintHistory.builder()
                    .sprint(sp)
                    .status(request.getStatus())
                    .note("Sprint status changed to " + request.getStatus())
                    .build());
        }

        SprintBackLog newSprint = this.sprintBackLogRepository.save(sp);

        return SprintBacklogResponseMapper.toResponse(newSprint, status);
    }
}
