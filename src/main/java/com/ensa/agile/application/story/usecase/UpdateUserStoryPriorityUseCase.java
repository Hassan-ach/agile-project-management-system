package com.ensa.agile.application.story.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.application.story.request.UserStoryUpdatePriorityRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

@Component
public class UpdateUserStoryPriorityUseCase extends BaseUseCase<UserStoryUpdatePriorityRequest, UserStoryResponse>{

    private final UserStoryRepository userStoryRepository;

    public UpdateUserStoryPriorityUseCase(ITransactionalWrapper tr, UserStoryRepository usr) {
        super(tr);
        this.userStoryRepository = usr;
    }

    @Override
    public UserStoryResponse execute(UserStoryUpdatePriorityRequest data) {
        UserStory userStory = userStoryRepository.findById(data.getId());

        userStory.updatePriority(data.getPriority());
        userStory = userStoryRepository.save(userStory);

        return UserStoryResponseMapper.toResponse(userStory);
    }

    
}
