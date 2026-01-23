package com.ensa.agile.application.user.security.service;

import com.ensa.agile.application.user.exception.InvalidCredentialsException;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface IAuthenticationService {
    String login(String email, String password)
        throws InvalidCredentialsException;

    String refreshToken(String refreshToken) throws InvalidCredentialsException;

    String generateRefreshToken(String token);

    String generateToken(String email);
}
