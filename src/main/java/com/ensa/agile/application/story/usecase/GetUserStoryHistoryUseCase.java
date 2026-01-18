package com.ensa.agile.application.story.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.repository.UserStoryHistoryRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

@Component
public class GetUserStoryHistoryUseCase extends BaseUseCase<UUID, List<UserStoryHistory>> {
    private final UserStoryHistoryRepository userStoryHistoryRepository;
    private final UserStoryRepository userStoryRepository;

    public GetUserStoryHistoryUseCase(ITransactionalWrapper tr,
    UserStoryHistoryRepository userStoryHistoryRepository,
    UserStoryRepository userStoryRepository) {
        super(tr);
        this.userStoryHistoryRepository = userStoryHistoryRepository;
        this.userStoryRepository = userStoryRepository;
    }

    public List<UserStoryHistory> execute(UUID id) {
        if(!userStoryRepository.existsById(id)) {
            throw new UserStoryNotFoundException();
        }

        return userStoryHistoryRepository.findAllByUserStoryId(id);
    }

    
}
