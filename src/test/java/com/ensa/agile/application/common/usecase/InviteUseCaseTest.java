package com.ensa.agile.application.common.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.common.request.InviteRequest;
import com.ensa.agile.application.common.response.InviteResponse;
import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.global.exception.BusinessRuleViolationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestProjectMemberFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
public class InviteUseCaseTest {

    private static class TestInviteUseCase extends InviteUseCase {
        public TestInviteUseCase(ProjectMemberRepository pmr,
                                 ProductBackLogRepository pbr,
                                 UserRepository ur, ICurrentUserService cus,
                                 ITransactionalWrapper tw) {
            super(pmr, pbr, ur, cus, tw);
        }

        @Override
        public InviteResponse execute(InviteRequest data) {
            return executeInvitaion(data);
        }
    }

    @Mock ITransactionalWrapper transactionalWrapper;
    @Mock ProjectMemberRepository projectMemberRepository;
    @Mock ProductBackLogRepository productBackLogRepository;
    @Mock UserRepository userRepository;
    @Mock
    ICurrentUserService currentUserService;

    @InjectMocks TestInviteUseCase inviteUseCase;

    @Test
    void execute_ShouldInviteProjectMember_WhenDataIsValid() {
        String invitedEmail = "invited@gmail.com";
        String currentEmail = "admin@gmail.com";

        InviteRequest request = new InviteRequest("productId123", invitedEmail);
        User invitedUser = TestUserFactory.validUserWithEmail(invitedEmail);
        User currentUser = TestUserFactory.validUserWithEmail(currentEmail);
        ProductBackLog pb = TestProductBackLogFactory.validProductWithId();
        ProjectMember invitation =
            TestProjectMemberFactory.validInvitedMember();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findByEmail(invitedEmail)).thenReturn(invitedUser);
        when(productBackLogRepository.findById(request.getProductId()))
            .thenReturn(pb);
        when(projectMemberRepository.existsByUserEmailAndProductBackLogId(
                 invitedEmail, request.getProductId()))
            .thenReturn(false);
        when(projectMemberRepository.save(any(ProjectMember.class)))
            .thenReturn(invitation);

        InviteResponse response = inviteUseCase.executeInvitaion(request);

        assertNotNull(response);
        assertEquals(invitation.getId(), response.getInviteId());
        assertTrue(response.isSuccess());

        verify(projectMemberRepository)
            .existsByUserEmailAndProductBackLogId(invitedEmail,
                                                  request.getProductId());
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    @Test
    void shouldThrowException_WhenUserInvitesThemselves() {
        String currentEmail = "self@test.com";
        User currentUser = TestUserFactory.validUserWithEmail(currentEmail);
        InviteRequest request = new InviteRequest("prod-123", currentEmail);

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        assertThrows(BusinessRuleViolationException.class,
                     () -> { inviteUseCase.executeInvitaion(request); });
    }
}
