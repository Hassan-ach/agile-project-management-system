package com.ensa.agile.application.epic.usecase;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.epic.request.EpicRequest;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteEpicUseCase
    extends BaseUseCase<EpicRequest, DeleteResponse> {
    private final EpicRepository epicRepository;

    public DeleteEpicUseCase(ITransactionalWrapper tr, EpicRepository ep) {
        super(tr);
        this.epicRepository = ep;
    }

    @Override
    public DeleteResponse execute(EpicRequest request) {

        if (!this.epicRepository.existsById(request.getEpicId())) {
            throw new EpicNotFoundException();
        }

        epicRepository.deleteById(request.getEpicId());

        return DeleteResponse.builder()
            .message("Epic deleted successfully")
            .success(true)
            .build();
    }
}
