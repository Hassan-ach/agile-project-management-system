package com.ensa.agile.application.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.request.AuthenticationRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.security.service.IAuthenticationService;
import com.ensa.agile.application.user.usecase.LoginUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock private IAuthenticationService authenticationService;

    @Mock private ITransactionalWrapper transactionalWrapper;

    @InjectMocks private LoginUseCase loginUseCase;

    @Test
    void execute_ShouldReturnToken_WhenCredentialsAreValid() {
        // Given
        String email = "user@ensa.ma";
        String password = "password123";
        String expectedToken = "valid.jwt.token";

        AuthenticationRequest request =
            new AuthenticationRequest(email, password);

        when(authenticationService.login(email, password))
            .thenReturn(expectedToken);

        // When
        AuthenticationResponse response = loginUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());

        // Verify
        verify(authenticationService, times(1)).login(email, password);
    }

    @Test
    void execute_ShouldThrowException_WhenAuthenticationFails() {
        // Given
        AuthenticationRequest request =
            new AuthenticationRequest("wrong@ensa.ma", "wrong");

        // Simulate a business exception thrown by the Port implementation
        when(authenticationService.login(anyString(), anyString()))
            .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // When / Then
        assertThrows(InvalidCredentialsException.class,
                     () -> loginUseCase.execute(request));
    }
}
