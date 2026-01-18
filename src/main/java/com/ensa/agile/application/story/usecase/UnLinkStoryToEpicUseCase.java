package com.ensa.agile.application.story.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.application.story.request.UserStoryEpicUpdateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

@Component
public class UnLinkStoryToEpicUseCase extends BaseUseCase<UserStoryEpicUpdateRequest, UserStoryResponse> {

    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;

    public UnLinkStoryToEpicUseCase(ITransactionalWrapper tr,
    EpicRepository er,
    UserStoryRepository usr) {
        super(tr);
        this.epicRepository = er;
        this.userStoryRepository = usr;
    }

    @Override 
    public UserStoryResponse execute(UserStoryEpicUpdateRequest data) {

        if(epicRepository.existsById(data.getEpicId())) {
            throw new EpicNotFoundException();
        }

        UserStory userStory = userStoryRepository.findById(data.getId());

        userStory.unlinkEpic();;

        userStory = userStoryRepository.save(userStory);

        return UserStoryResponseMapper.toResponse(userStory);
    }
}
