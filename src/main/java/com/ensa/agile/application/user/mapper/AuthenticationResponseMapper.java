package com.ensa.agile.application.user.mapper;

import com.ensa.agile.application.user.response.AuthenticationResponse;

public class AuthenticationResponseMapper {

    public static AuthenticationResponse toResponse(String accesToken,
                                                    String refreshToken) {
        return new AuthenticationResponse(accesToken, refreshToken);
    }
}
