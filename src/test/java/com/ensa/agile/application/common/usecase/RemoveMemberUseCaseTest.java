package com.ensa.agile.application.product.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.common.usecase.RemoveUseCase;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RemoveMemberUseCaseTest {

    private static class TestRemoveMemberUseCase extends RemoveUseCase {
        public TestRemoveMemberUseCase(ITransactionalWrapper tr,
                                       ProjectMemberRepository pm,
                                       UserRepository ur) {
            super(tr, pm, ur);
        }
        public RemoveResponse execute(RemoveRequest request) {
            return removeUser(request);
        }
    }

    @Mock ITransactionalWrapper transactionalWrapper;
    @Mock ProjectMemberRepository projectMemberRepository;
    @Mock UserRepository userRepository;

    @InjectMocks TestRemoveMemberUseCase removeMemberUseCase;

    @Test
    void execute_ShouldRemoveProjectMember_WhenDataIsValid() {
        RemoveRequest request =
            new RemoveRequest("productId", "test@gmail.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(
            projectMemberRepository.existsByUserEmailAndProductBackLogIdAndRole(
                anyString(), anyString(), any(RoleType.class)))
            .thenReturn(true);

        RemoveResponse response = removeMemberUseCase.execute(request);

        assertNotNull(response);
        assertEquals(true, response.isSuccess());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(projectMemberRepository)
            .existsByUserEmailAndProductBackLogIdAndRole(
                request.getEmail(), request.getProductId(), RoleType.MEMBER);
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        RemoveRequest request =
            new RemoveRequest("productId", "test@gmail.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                     () -> removeMemberUseCase.execute(request));
    }

    @Test
    void execute_ShouldFail_WhenUserNotMemberOrRoleMisMatch() {

        RemoveRequest request =
            new RemoveRequest("productId", "test@gmail.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(
            projectMemberRepository.existsByUserEmailAndProductBackLogIdAndRole(
                anyString(), anyString(), any(RoleType.class)))
            .thenReturn(false);

        RemoveResponse response = removeMemberUseCase.execute(request);

        assertNotNull(response);
        assertEquals(false, response.isSuccess());

        verify(userRepository).existsByEmail(request.getEmail());
    }
}
