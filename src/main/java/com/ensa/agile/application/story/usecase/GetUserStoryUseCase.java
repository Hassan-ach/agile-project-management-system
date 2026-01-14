package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.global.service.IFetchService;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

public class GetUserStoryUseCase
    extends BaseUseCase<UserStoryGetRequest, UserStoryResponse> {

    private UserStoryRepository userStoryRepository;
    private IFetchService fetchService;
    public GetUserStoryUseCase(ITransactionalWrapper tr,
                               UserStoryRepository userStoryRepository,
                               IFetchService fetchService) {
        super(tr);
        this.userStoryRepository = userStoryRepository;
        this.fetchService = fetchService;
    }

    @Override
    public UserStoryResponse execute(UserStoryGetRequest request) {
        if (!userStoryRepository.existsById(request.getId())) {
            throw new UserStoryNotFoundException();
        }
        return fetchService.getResponse(request);
    }
}
