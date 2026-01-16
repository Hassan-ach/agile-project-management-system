package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.common.response.InviteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.request.InviteSprintMemberRequest;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintMember;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintMembersRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class InviteSprintMemberUseCase
    extends BaseUseCase<InviteSprintMemberRequest, InviteResponse> {

    private final SprintMembersRepository sprintMemberRepository;
    private final SprintBackLogRepository sprintBackLogRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProductBackLogRepository productBackLogRepository;

    public InviteSprintMemberUseCase(
        ITransactionalWrapper tr,
        SprintMembersRepository sprintMemberRepository,
        SprintBackLogRepository sprintBackLogRepository,
        ProjectMemberRepository projectMemberRepository,
        UserRepository userRepository,
        ProductBackLogRepository productBackLogRepository) {

        super(tr);
        this.sprintMemberRepository = sprintMemberRepository;
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
        this.productBackLogRepository = productBackLogRepository;
    }

    @Override
    public InviteResponse execute(InviteSprintMemberRequest request) {

        User user = userRepository.findByEmail(request.getEmail());
        ProductBackLog pb =
            productBackLogRepository.findById(request.getProductId());
        SprintBackLog sp =
            sprintBackLogRepository.findById(request.getSprintId());

        if (!projectMemberRepository.existsByUserEmailAndProductBackLogId(
                request.getEmail(), request.getProductId())) {
            projectMemberRepository.save(ProjectMember.builder()
                                             .user(user)
                                             .productBackLog(pb)
                                             .role(RoleType.DEVELOPER)
                                             .build());
        }

        sprintMemberRepository.save(
            SprintMember.builder().user(user).sprintBackLog(sp).build());
        return null;
    }
}
