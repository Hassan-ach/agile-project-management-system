package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.application.story.request.UserStoryCreateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.story.repository.UserStoryHistoryRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateUserStoryUseCase
    extends BaseUseCase<UserStoryCreateRequest, UserStoryResponse> {

    private final UserStoryRepository userStoryRepository;
    private final ProductBackLogRepository productBackLogRepository;
    private final UserStoryHistoryRepository userStoryHistoryRepository;

    public CreateUserStoryUseCase(ITransactionalWrapper tr,
                                  UserStoryRepository usr,
                                  ProductBackLogRepository pbr,
                                  UserStoryHistoryRepository ushr) {
        super(tr);
        this.userStoryRepository = usr;
        this.productBackLogRepository = pbr;
        this.userStoryHistoryRepository = ushr;
    }

    @Override
    public UserStoryResponse execute(UserStoryCreateRequest request) {
        ProductBackLog pb =
            this.productBackLogRepository.findById(request.getProductId());

        UserStory us = this.userStoryRepository.save(
            UserStory.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .storyPoints(request.getStoryPoints())
                .acceptanceCriteria(request.getAcceptanceCriteria())
                .productBackLog(pb)
                .build());

        UserStoryHistory status = this.userStoryHistoryRepository.save(
            UserStoryHistory.builder()
                .userStory(us)
                .note("User Story created with title: " + request.getTitle())
                .status(StoryStatus.TODO)
                .build());

        us.setStatus(status);

        return UserStoryResponseMapper.toResponse(us);
    }
}
