package com.ensa.agile.application.sprint.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;

@Component
public class DeleteSprintBackLogUseCase
    extends BaseUseCase<String, DeleteResponse> {
    private SprintBackLogRepository sprintBackLogRepository;

    public DeleteSprintBackLogUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
    }

    @Override
    public DeleteResponse execute(String request) {
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
