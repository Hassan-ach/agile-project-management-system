package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveRequest {
    private String email;
    private String productId;

    public RemoveRequest(String productId, RemoveRequest req) {
        this.productId = productId;
        this.email = req.getEmail();

        validate();
    }

    public RemoveRequest(String email) {
        this.email = email;
        this.productId = null;

        validate();
    }

    public void validate() {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("Email format is invalid");
        }
        if (productId == null || productId.isBlank()) {
            throw new ValidationException("Product ID cannot be null or empty");
        }
    }
}
