package com.ensa.agile.application.user.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import com.ensa.agile.testfactory.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetUserInfoUseCaseTest {

    @Mock private ITransactionalWrapper transactionalWrapper;
    @Mock private UserRepository userRepository;
    @InjectMocks private GetUserInfoUseCase getUserInfoUseCase;

    @Test
    void execute_ShouldReturnUser_WhenEmailExists() {
        // Given
        User user = TestUserFactory.validUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        var result = getUserInfoUseCase.execute(user.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(result.getEmail(), user.getEmail());

        // Verify
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void execute_ShouldThrowException_WhenEmailDoesNotExist() {
        String email = "test@gmail.com";

        when(userRepository.findByEmail(email))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,
                     () -> getUserInfoUseCase.execute(email));
    }
}
