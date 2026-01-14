package com.ensa.agile.presentation.controller;

import com.ensa.agile.application.user.request.AuthenticationRequest;
import com.ensa.agile.application.user.request.RefreshTokenRequest;
import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.usecase.LoginUseCase;
import com.ensa.agile.application.user.usecase.RefreshTokenUseCase;
import com.ensa.agile.application.user.usecase.RegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request) {

        AuthenticationResponse response =
            registerUseCase.executeTransactionally(
                new RegisterRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>
    login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response =
            loginUseCase.execute(new AuthenticationRequest(request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse>
    refresh(@RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response =
            refreshTokenUseCase.executeTransactionally(
                new RefreshTokenRequest(request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
