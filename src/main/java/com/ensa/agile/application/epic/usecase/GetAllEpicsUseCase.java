package com.ensa.agile.application.epic.usecase;

import com.ensa.agile.application.epic.mapper.EpicResponseMapper;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import java.util.List;
import java.util.UUID;

public class GetAllEpicsUseCase extends BaseUseCase<UUID, List<EpicResponse>> {

    private final EpicRepository epicRepository;

    public GetAllEpicsUseCase(ITransactionalWrapper tr, EpicRepository epr) {
        super(tr);
        this.epicRepository = epr;
    }

    @Override
    public List<EpicResponse> execute(UUID productId) {

        return epicRepository.findAllByProductBackLogId(productId)
            .stream()
            .map(EpicResponseMapper::toResponse)
            .toList();
    }
}
