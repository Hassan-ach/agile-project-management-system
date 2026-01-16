package com.ensa.agile.application.product.usecase;

import com.ensa.agile.application.common.request.InviteRequest;
import com.ensa.agile.application.common.response.InviteResponse;
import com.ensa.agile.application.common.usecase.InviteUseCase;
import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.repository.UserRepository;

public class InviteDeveloperUseCase extends InviteUseCase {

    public InviteDeveloperUseCase(
        ProjectMemberRepository projectMemberRepository,
        ProductBackLogRepository productBackLogRepository,
        UserRepository userRepository, ICurrentUserService currentUserService,
        ITransactionalWrapper transactionalWrapper) {

        super(projectMemberRepository, productBackLogRepository, userRepository,
              currentUserService, transactionalWrapper);
    }

    public InviteResponse execute(InviteRequest data) {
        return this.executeInvitaion(data, RoleType.DEVELOPER);
    }
}
