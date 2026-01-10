
package com.ensa.agile.application.product.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.product.mapper.ProductBackLogResponseMapper;
import com.ensa.agile.application.product.request.ProductBackLogUpdateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductBackLogInfoUseCase
    extends BaseUseCase<ProductBackLogUpdateRequest, ProductBackLogResponse> {

    private final ProductBackLogRepository productBackLogRepository;

    public UpdateProductBackLogInfoUseCase(
        ProductBackLogRepository productBackLogRepository,
        ITransactionalWrapper transactionalWrapper) {
        super(transactionalWrapper);
        this.productBackLogRepository = productBackLogRepository;
    }

    public ProductBackLogResponse execute(ProductBackLogUpdateRequest data) {
        ProductBackLog productBackLog =
            productBackLogRepository.findById(data.getId());

        productBackLog.updateMetadata(data.getName(), data.getDescription());

        return ProductBackLogResponseMapper.toResponse(
            productBackLogRepository.save(productBackLog));
    }
}
