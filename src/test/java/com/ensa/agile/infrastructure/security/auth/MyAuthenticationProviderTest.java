package com.ensa.agile.infrastructure.security.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import com.ensa.agile.testfactory.TestUserFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@Transactional
class MyAuthenticationProviderTest {

    @Mock private UserRepository userRepository;

    @Mock private IPasswordEncoder passwordEncoder;

    @InjectMocks private MyAuthenticationProvider authProvider;

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsMatch() {
        String email = "hassan@ensa.ma";
        String password = "hashed_password";
        User mockUser = User.builder()
                            .firstName("test")
                            .lastName("test")
                            .email(email)
                            .password(password)
                            .build();

        Authentication inputAuth =
            new UsernamePasswordAuthenticationToken(email, "raw_password");

        when(userRepository.findByEmail(email)).thenReturn(mockUser);
        when(passwordEncoder.matches("raw_password", password))
            .thenReturn(true);

        Authentication result = authProvider.authenticate(inputAuth);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals(mockUser, result.getPrincipal());
    }

    @Test
    void authenticate_ShouldThrowInvalidCredentials_WhenPasswordMismatches() {
        User mockUser = TestUserFactory.validUser();

        Authentication inputAuth = new UsernamePasswordAuthenticationToken(
            "test2@gmil.com", "wrong_pass");

        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(passwordEncoder.matches("wrong_pass", mockUser.getPassword()))
            .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                     () -> authProvider.authenticate(inputAuth));
    }

    @Test
    void supports_ShouldReturnTrue_ForUsernamePasswordToken() {
        assertTrue(
            authProvider.supports(UsernamePasswordAuthenticationToken.class));

        assertFalse(authProvider.supports(AnonymousAuthenticationToken.class));
    }
}
