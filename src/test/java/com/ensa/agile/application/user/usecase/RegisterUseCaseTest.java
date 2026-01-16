package com.ensa.agile.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.user.exception.EmailAlreadyUsedException;
import com.ensa.agile.application.user.request.RegisterRequest;
import com.ensa.agile.application.user.response.AuthenticationResponse;
import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.application.user.security.service.IAuthenticationService;
import com.ensa.agile.application.user.usecase.RegisterUseCase;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {

    @Mock private ITransactionalWrapper transactionalWrapper;
    @Mock private UserRepository userRepository;
    @Mock private IPasswordEncoder passwordEncoder;
    @Mock private IAuthenticationService authenticationService;
    @InjectMocks private RegisterUseCase registerUseCase;

    @Test
    void execute_ShouldSuccessfullyRegisterUser_WhenEmailDoesNotExist() {
        // Given
        RegisterRequest request =
            new RegisterRequest("John", "Doe", "test@gmail.com", "Password123");

        String encodedPassword = "EncodedPassword123";
        String jwtToken = "jwt_token";

        when(userRepository.existsByEmail(request.getEmail()))
            .thenReturn(false);
        when(passwordEncoder.encode(request.getPassword()))
            .thenReturn(encodedPassword);
        when(authenticationService.generateToken(request.getEmail()))
            .thenReturn(jwtToken);

        // When
        AuthenticationResponse response = registerUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertEquals(response.getAccessToken(), jwtToken);

        // Verify
        verify(userRepository)
            .save(argThat(user
                          -> user.getEmail().equals(request.getEmail()) &&
                                 user.getPassword().equals(encodedPassword)));
    }

    @Test
    void execute_ShouldThrowException_WhenEmailAlreadyInUse() {
        // Given
        RegisterRequest request =
            new RegisterRequest("John", "Doe", "test@gmail.com", "Password123");

        // When
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Then / Assert
        assertThrows(EmailAlreadyUsedException.class,
                     () -> registerUseCase.execute(request));

        // Verify
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
        verify(authenticationService, never()).generateToken(anyString());
    }
}
