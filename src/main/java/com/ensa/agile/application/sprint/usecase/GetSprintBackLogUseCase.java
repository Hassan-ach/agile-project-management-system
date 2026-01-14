package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.global.service.IFetchService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import org.springframework.stereotype.Component;

@Component
public class GetSprintBackLogUseCase
    extends BaseUseCase<SprintBackLogGetRequest, SprintBackLogResponse> {
    private SprintBackLogRepository sprintBackLogRepository;
    private IFetchService fetchService;
    public GetSprintBackLogUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository,
        IFetchService fetchService) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.fetchService = fetchService;
    }

    @Override
    public SprintBackLogResponse execute(SprintBackLogGetRequest request) {
        if (!sprintBackLogRepository.existsById(request.getId())) {
            throw new SprintBackLogNotFoundException();
        }
        return fetchService.getResponse(request);
    }
}
