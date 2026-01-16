package com.ensa.agile.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ensa.agile.application.global.service.ICurrentUser;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.product.request.ProductBackLogCreateRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.product.usecase.CreateProductBackLogUseCase;
import com.ensa.agile.domain.global.exception.DataBasePersistenceException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.testfactory.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateProductBackLogUseCaseTest {

    @Mock private ITransactionalWrapper transactionalWrapper;
    @Mock private ProductBackLogRepository productBackLogRepository;
    @Mock private ProjectMemberRepository projectMemberRepository;
    @Mock private ICurrentUser currentUserService;

    @InjectMocks
    private CreateProductBackLogUseCase createProductBackLogUseCase;

    @Test
    void execute_ShouldCreateProductBackLog_WhenDataIsValid() {
        ProductBackLogCreateRequest request =
            new ProductBackLogCreateRequest("New Backlog", "Project123");
        User user = TestUserFactory.validUser();
        ProductBackLog savedProductBackLog =
            ProductBackLog.builder()
                .id("Backlog123")
                .name(request.getName())
                .description(request.getDescription())
                .build();
        ProjectMember projectMember = ProjectMember.builder()
                                          .id("Member123")
                                          .user(user)
                                          .productBackLog(savedProductBackLog)
                                          .role(RoleType.PRODUCT_OWNER)
                                          .status(MemberStatus.ACTIVE)
                                          .build();
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(productBackLogRepository.save(any(ProductBackLog.class)))
            .thenReturn(savedProductBackLog);
        when(projectMemberRepository.save(any(ProjectMember.class)))
            .thenReturn(projectMember);

        ProductBackLogResponse response =
            createProductBackLogUseCase.execute(request);

        assertNotNull(response);
        assertEquals(response.getId(), savedProductBackLog.getId());

        verify(productBackLogRepository)
            .save(argThat(
                pb
                -> pb.getName().equals(request.getName()) &&
                       pb.getDescription().equals(request.getDescription())));

        ArgumentCaptor<ProjectMember> projectMemberCaptor =
            ArgumentCaptor.forClass(ProjectMember.class);
        verify(projectMemberRepository).save(projectMemberCaptor.capture());
        ProjectMember savedMember = projectMemberCaptor.getValue();
        assertEquals(savedMember.getUser(), user);
        assertEquals(savedMember.getProductBackLog(), savedProductBackLog);
        assertEquals(savedMember.getRole(), RoleType.PRODUCT_OWNER);
        assertEquals(savedMember.getStatus(), MemberStatus.ACTIVE);
    }

    @Test
    void execute_ShouldThrowException_WhenProductBackLogFailedToPersist() {
        ProductBackLogCreateRequest request =
            new ProductBackLogCreateRequest("New Backlog", "Project123");
        when(productBackLogRepository.save(any(ProductBackLog.class)))
            .thenThrow(DataBasePersistenceException.class);

        assertThrows(DataBasePersistenceException.class,
                     () -> createProductBackLogUseCase.execute(request));

        verify(productBackLogRepository)
            .save(argThat(
                pb
                -> pb.getName().equals(request.getName()) &&
                       pb.getDescription().equals(request.getDescription())));

        verify(currentUserService, times(0)).getCurrentUser();
        verify(projectMemberRepository, times(0))
            .save(any(ProjectMember.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserContextIsInvalid() {
        // Test implementation goes here
    }

    @Test
    void execute_ShouldThrowException_WhenProjectMemberFailedToPersist() {
        // Test implementation goes here
    }
}
