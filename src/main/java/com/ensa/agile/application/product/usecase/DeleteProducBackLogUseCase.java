package com.ensa.agile.application.product.usecase;

import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class DeleteProducBackLogUseCase
    extends BaseUseCase<UUID, RemoveResponse> {
    private ProductBackLogRepository productBackLogRepository;

    public DeleteProducBackLogUseCase(ITransactionalWrapper tr,
                                      ProductBackLogRepository pr) {
        super(tr);
        this.productBackLogRepository = pr;
    }

    @Override
    public RemoveResponse execute(UUID id) {
        if (!productBackLogRepository.existsById(id)) {
            throw new ProductBackLogNotFoundException();
        }

        productBackLogRepository.deleteById(id);

        return RemoveResponse.builder()
            .message("Product BackLog deleted successfully")
            .success(true)
            .build();
    }
}
