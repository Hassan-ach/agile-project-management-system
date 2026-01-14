package com.ensa.agile.infrastructure.security.service;

import com.ensa.agile.application.user.exception.AuthenticationFailureException;
import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.security.service.IAuthenticationService;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticationService implements IAuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationProvider provider;

    @Override
    public String login(String email, String password)
        throws AuthenticationFailureException {
        try {

            Authentication auth = this.provider.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
            if (auth == null || !auth.isAuthenticated()) {
                throw new InvalidCredentialsException(
                    "Authentication failed unexpectedly.");
            }
            return jwtService.generateToken((User)auth.getPrincipal());

        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
    }

    @Override
    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    @Override
    public String generateRefreshToken(String token) {
        return jwtService.generateRefreshToken(token);
    }

    @Override
    public String refreshToken(String refreshToken)
        throws InvalidCredentialsException {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new InvalidCredentialsException("Refresh token expired");
        }
        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email);
        return this.generateToken(user.getEmail());
    }
}
