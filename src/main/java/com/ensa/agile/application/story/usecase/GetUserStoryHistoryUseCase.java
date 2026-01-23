package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.application.story.mapper.UserStoryHistoryResponseMapper;
import com.ensa.agile.application.story.response.UserStoryHistoryResponse;
import com.ensa.agile.domain.story.repository.UserStoryHistoryRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GetUserStoryHistoryUseCase
    extends BaseUseCase<UUID, List<UserStoryHistoryResponse>> {
    private final UserStoryHistoryRepository userStoryHistoryRepository;
    private final UserStoryRepository userStoryRepository;

    public GetUserStoryHistoryUseCase(
        ITransactionalWrapper tr,
        UserStoryHistoryRepository userStoryHistoryRepository,
        UserStoryRepository userStoryRepository) {
        super(tr);
        this.userStoryHistoryRepository = userStoryHistoryRepository;
        this.userStoryRepository = userStoryRepository;
    }

    public List<UserStoryHistoryResponse> execute(UUID id) {
        if (!userStoryRepository.existsById(id)) {
            throw new UserStoryNotFoundException();
        }

        return userStoryHistoryRepository.findAllByUserStoryId(id)
            .stream()
            .map(UserStoryHistoryResponseMapper::toResponse)
            .toList();
    }
}
