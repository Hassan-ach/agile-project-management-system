package com.ensa.agile.application.common.usecase;

import com.ensa.agile.application.common.request.InviteRequest;
import com.ensa.agile.application.common.response.InviteResponse;
import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.user.exception.UserAlreadyInvitedException;
import com.ensa.agile.domain.global.exception.BusinessRuleViolationException;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import java.util.UUID;

public abstract class InviteUseCase
    extends BaseUseCase<InviteRequest, InviteResponse> {

    private final ProductBackLogRepository productBackLogRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ICurrentUserService currentUserService;

    public InviteUseCase(ProjectMemberRepository projectMemberRepository,
                         ProductBackLogRepository productBackLogRepository,
                         UserRepository userRepository,
                         ICurrentUserService currentUserService,
                         ITransactionalWrapper transactionalWrapper) {

        super(transactionalWrapper);
        this.projectMemberRepository = projectMemberRepository;
        this.productBackLogRepository = productBackLogRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    abstract public InviteResponse execute(InviteRequest data);

    public InviteResponse executeInvitaion(InviteRequest data) {
        return invite(data, RoleType.MEMBER);
    }

    public InviteResponse executeInvitaion(InviteRequest data,
                                           RoleType roleType) {
        return invite(data, roleType);
    }

    private InviteResponse invite(InviteRequest data, RoleType roleType) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getEmail() == data.getEmail()) {
            throw new BusinessRuleViolationException(
                "You cannot invite yourself as " + roleType);
        }

        User u = userRepository.findByEmail(data.getEmail());

        if (isUserAlreadyInvited(data.getEmail(), data.getProductId())) {
            throw new UserAlreadyInvitedException();
        }

        ProjectMember invitedSaved = projectMemberRepository.save(
            ProjectMember.builder()
                .user(u)
                .productBackLog(
                    productBackLogRepository.findById(data.getProductId()))
                .role(roleType)
                .status(MemberStatus.INVITED)
                .build());

        return InviteResponse.builder()
            .message("User " + u.getEmail() + " invited successfully as " +
                     roleType)
            .success(true)
            .inviteId(invitedSaved.getId())
            .build();
    }

    private boolean isUserAlreadyInvited(String email, UUID productId) {
        return projectMemberRepository.existsByUserEmailAndProductBackLogId(
            email, productId);
    }
}
