package com.ensa.agile.application.sprint.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;

@Component
public class GetSprintHistoryUseCase extends BaseUseCase<UUID, List<SprintHistory>> {
    private final SprintHistoryRepository sprintHistoryRepository;
    private final SprintBackLogRepository sprintRepository;

    public GetSprintHistoryUseCase(ITransactionalWrapper tr,
    SprintHistoryRepository sprintHistoryRepository,
    SprintBackLogRepository sprintRepository) {
        super(tr);
        this.sprintHistoryRepository = sprintHistoryRepository;
        this.sprintRepository = sprintRepository;
    }

    public List<SprintHistory> execute(UUID id) {
        if(!sprintRepository.existsById(id)) {
            throw new SprintBackLogNotFoundException();
        }

        return sprintHistoryRepository.findAllBySprintId(id);
    }

    
}
