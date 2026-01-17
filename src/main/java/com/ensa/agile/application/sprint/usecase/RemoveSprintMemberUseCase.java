package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.common.response.RemoveResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.request.RemoveSprintMemberRequest;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.sprint.repository.SprintMembersRepository;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RemoveSprintMemberUseCase
    extends BaseUseCase<RemoveSprintMemberRequest, RemoveResponse> {

    private final SprintMembersRepository sprintMembersRepository;
    private final UserRepository userRepository;

    public RemoveSprintMemberUseCase(
        ITransactionalWrapper tr,
        SprintMembersRepository sprintMembersRepository,
        UserRepository userRepository) {

        super(tr);
        this.sprintMembersRepository = sprintMembersRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RemoveResponse execute(RemoveSprintMemberRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new UserNotFoundException();
        }

        sprintMembersRepository.deleteByUserIdAndSprintBackLogId(
            request.getUserId(), request.getSprintId());

        return RemoveResponse.builder()
            .message("Sprint member removed successfully")
            .success(true)
            .build();
    }
}
