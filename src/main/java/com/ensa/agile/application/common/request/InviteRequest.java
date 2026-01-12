package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class InviteRequest {
    private String productId;
    private String email;

    public InviteRequest(String productId, InviteRequest req) {
        this.productId = productId;
        this.email = req.getEmail();
    }

    public InviteRequest(String productId, String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("Email format is invalid");
        }

        if (productId == null || productId.isBlank()) {
            throw new ValidationException("Product ID cannot be null or empty");
        }
        this.productId = productId;
        this.email = email;
    }

    public InviteRequest(String email) {
        this.email = email;
        this.productId = null;
    }
}
