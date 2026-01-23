package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.application.story.request.UserStoryUpdateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserStoryUseCase
    extends BaseUseCase<UserStoryUpdateRequest, UserStoryResponse> {

    private final UserStoryRepository userStoryRepository;

    public UpdateUserStoryUseCase(ITransactionalWrapper tr,
                                  UserStoryRepository userStoryRepository) {

        super(tr);
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public UserStoryResponse execute(UserStoryUpdateRequest request) {
        UserStory us = this.userStoryRepository.findById(request.getId());

        us.updateMetaData(request.getTitle(), request.getDescription(),
                          request.getPriority(), request.getStoryPoints(),
                          request.getAcceptanceCriteria());

        UserStory newUserStory = this.userStoryRepository.save(us);

        return UserStoryResponseMapper.toResponse(newUserStory);
    }
}
