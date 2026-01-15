package com.ensa.agile.application.user.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.user.mapper.AuthenticationResponseMapper;
import com.ensa.agile.application.user.request.RefreshTokenRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.security.service.IAuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenUseCase
    extends BaseUseCase<RefreshTokenRequest, AuthenticationResponse> {
    private final IAuthenticationService autenticationService;

    public RefreshTokenUseCase(ITransactionalWrapper tr,
                               IAuthenticationService authenticationService) {
        super(tr);
        this.autenticationService = authenticationService;
    }

    @Override
    public AuthenticationResponse execute(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        String accessToken = autenticationService.refreshToken(refreshToken);
        String newRefreshToken =
            autenticationService.generateRefreshToken(accessToken);

        return AuthenticationResponseMapper.toResponse(accessToken,
                                                       newRefreshToken);
    }
}
