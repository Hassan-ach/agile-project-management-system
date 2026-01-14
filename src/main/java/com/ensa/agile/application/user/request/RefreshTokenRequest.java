package com.ensa.agile.application.user.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    String refreshToken;

    public RefreshTokenRequest(RefreshTokenRequest req) {
        if (req == null || req.refreshToken == null ||
            req.refreshToken.trim().isBlank()) {
            throw new ValidationException(
                "Refresh token cannot be null or blank");
        }
        this.refreshToken = req.refreshToken;
    }
}
