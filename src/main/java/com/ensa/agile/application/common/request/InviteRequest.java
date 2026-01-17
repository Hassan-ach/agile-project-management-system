package com.ensa.agile.application.common.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
public class InviteRequest {
    private UUID productId;
    private String email;

    public InviteRequest(UUID productId, InviteRequest req) {
        this.productId = productId;
        this.email = req.getEmail();
        validate();
    }

    public InviteRequest(UUID productId, String email) {
        this.productId = productId;
        this.email = email;

        validate();
    }

    public InviteRequest(String email) {
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
        if (productId == null) {
            throw new ValidationException("Product ID cannot be null ");
        }
    }
}
