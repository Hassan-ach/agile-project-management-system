package com.ensa.agile.application.common.usecase;

import com.ensa.agile.application.common.request.RemoveRequest;
import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.user.repository.UserRepository;
import java.util.UUID;

public abstract class RemoveUseCase
    extends BaseUseCase<RemoveRequest, RemoveResponse> {

    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    public RemoveUseCase(ITransactionalWrapper tr, ProjectMemberRepository pm,
                         UserRepository ur) {
        super(tr);
        this.projectMemberRepository = pm;
        this.userRepository = ur;
    }

    abstract public RemoveResponse execute(RemoveRequest request);

    public RemoveResponse removeUser(RemoveRequest request) {

        return removeUserWithRole(request, RoleType.MEMBER);
    }

    public RemoveResponse removeUserWithRole(RemoveRequest request,
                                             RoleType role) {

        if (!isUserExists(request.getUserId())) {
            throw new UserNotFoundException();
        }

        if (!hasRole(request.getUserId(), request.getProductId(), role)) {
            return RemoveResponse.builder()
                .message("User is not a member of the project")
                .success(false)
                .build();
        }

        projectMemberRepository.deleteByUserIdAndProductBackLogId(
            request.getUserId(), request.getProductId());

        return RemoveResponse.builder()
            .message("User removed successfully")
            .success(true)
            .build();
    }

    private boolean isUserExists(UUID userId) {
        return userRepository.existsById(userId);
    }

    private boolean hasRole(UUID userId, UUID projectId, RoleType role) {

        if (role == RoleType.MEMBER) {
            return projectMemberRepository.existsByUserIdAndProductBackLogId(
                userId, projectId);
        }

        return projectMemberRepository
            .existsByUserIdAndProductBackLogIdAndRole(userId, projectId,
                                                         role);
    }
}
