package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintBackLogResponseMapper;
import com.ensa.agile.application.sprint.request.SprintBackLogUpdateRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateSprintBackLogUseCase
    extends BaseUseCase<SprintBackLogUpdateRequest, SprintBackLogResponse> {

    private final SprintBackLogRepository sprintBackLogRepository;
    private final UserRepository userRepository;

    public UpdateSprintBackLogUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository,
        UserRepository userRepository) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
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

        SprintBackLog newSprint = this.sprintBackLogRepository.save(sp);

        return SprintBackLogResponseMapper.toResponse(newSprint);
    }
}
