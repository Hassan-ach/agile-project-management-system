package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DeleteSprintBackLogUseCase
    extends BaseUseCase<UUID, DeleteResponse> {
    private final SprintBackLogRepository sprintBackLogRepository;

    public DeleteSprintBackLogUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
    }

    @Override
    public DeleteResponse execute(UUID request) {
        if (!sprintBackLogRepository.existsById(request)) {
            throw new SprintBackLogNotFoundException();
        }

        sprintBackLogRepository.deleteById(request);
        return DeleteResponse.builder()
            .success(true)
            .message("Sprint BackLog deleted successfully")
            .build();
    }
}
