package com.ensa.agile.application.epic.request;

import com.ensa.agile.application.common.request.GetRequest;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.UUID;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class EpicGetRequest extends GetRequest {
    UUID productId;

    public EpicGetRequest(UUID productId, UUID id, String with) {
        super(id, with);
        if (productId == null) {
            throw new ValidationException("productId cannot be null");
        }
        this.productId = productId;
    }
}
