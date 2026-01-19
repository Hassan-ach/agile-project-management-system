package com.ensa.agile.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensa.agile.application.user.request.AuthenticationRequest;
import com.ensa.agile.application.user.request.RefreshTokenRequest;
import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.response.UserInfoResponse;
import com.ensa.agile.application.user.usecase.GetUserInfoUseCase;
import com.ensa.agile.application.user.usecase.LoginUseCase;
import com.ensa.agile.application.user.usecase.RefreshTokenUseCase;
import com.ensa.agile.application.user.usecase.RegisterUseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;

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

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me() {
        UserInfoResponse response =
            getUserInfoUseCase.executeTransactionally(null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
            }
}
