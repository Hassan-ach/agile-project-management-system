package com.ensa.agile.application.epic.usecase;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.global.service.IFetchService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import org.springframework.stereotype.Component;

@Component
public class GetEpicUseCase extends BaseUseCase<EpicGetRequest, EpicResponse> {
    private final EpicRepository epicRepository;
    private final IFetchService fetchService;

    public GetEpicUseCase(ITransactionalWrapper tr,
                          EpicRepository epicRepository,
                          IFetchService fetchService) {
        super(tr);
        this.epicRepository = epicRepository;
        this.fetchService = fetchService;
    }

    @Override
    public EpicResponse execute(EpicGetRequest req) {

        if (!epicRepository.existsById(req.getId())) {
            throw new EpicNotFoundException();
        }

        return fetchService.getResponse(req);
    }
}
