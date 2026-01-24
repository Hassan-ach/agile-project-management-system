package com.ensa.agile.application.common.request;

import java.util.UUID;

import com.ensa.agile.domain.global.utils.ValidationUtil;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
public class InviteRequest {
    private UUID productId;
    private String email;

    public InviteRequest(UUID productId, String email) {
        this.productId = productId;
        this.email = email;

        validate();
    }

    public InviteRequest(String email) { this.email = email; }

    public void validate() {
        ValidationUtil.requireValidEmail(email, "user email");
        ValidationUtil.requireNonNull(productId, "product id");
    }
}
