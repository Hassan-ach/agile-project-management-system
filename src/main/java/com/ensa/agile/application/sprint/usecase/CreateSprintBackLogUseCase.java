package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintBacklogResponseMapper;
import com.ensa.agile.application.sprint.request.SprintBackLogCreateRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CreateSprintBackLogUseCase
    extends BaseUseCase<SprintBackLogCreateRequest, SprintBackLogResponse> {
    private final SprintBackLogRepository sprintBackLogRepository;
    private final ProductBackLogRepository productBackLogRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintHistoryRepository sprintHistoryRepository;

    public CreateSprintBackLogUseCase(ITransactionalWrapper tr,
                                      SprintBackLogRepository sblr,
                                      ProductBackLogRepository pbr,
                                      UserStoryRepository usr,
                                      SprintHistoryRepository shr) {
        super(tr);
        this.sprintBackLogRepository = sblr;
        this.productBackLogRepository = pbr;
        this.userStoryRepository = usr;
        this.sprintHistoryRepository = shr;
    }

    public SprintBackLogResponse execute(SprintBackLogCreateRequest request) {

        List<UserStory> userStories =
            this.userStoryRepository.findByBatch(request.getUserStoriesIds());

        // validate the request
        validate(userStories, request);

        SprintBackLog sprint = this.sprintBackLogRepository.save(
            SprintBackLog.builder()
                .name(request.getName())
                .productBackLog(this.productBackLogRepository.findById(
                    request.getProductId()))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .goal(request.getGoal())
                .build());

        SprintHistory status = this.sprintHistoryRepository.save(
            SprintHistory.builder()
                .sprint(sprint)
                .status(SprintStatus.PLANNED)
                .note("new Sprint with name of " + request.getName() +
                      " was created")
                .build());

        this.userStoryRepository.assignToSprint(request.getUserStoriesIds(),
                                                sprint);

        return SprintBacklogResponseMapper.toResponse(sprint, userStories,
                                                      status);
    }

    // this function to make sure that all user stories are exists and not
    // assigned to any sprint
    private void validate(List<UserStory> userStories,
                          SprintBackLogCreateRequest request) {
        // validate that all user stories exist
        if (userStories.size() != request.getUserStoriesIds().size()) {
            throw new UserStoryNotFoundException(
                "One or more User Stories not found");
        }

        // validate that all user stories belong to the same product backlog
        String productId = request.getProductId();
        for (UserStory us : userStories) {
            if (!us.getProductBackLog().getId().equals(productId)) {
                throw new ValidationException(
                    "All User Stories must belong to the same Product Backlog");
            }
        }

        // validate that no user story is already assigned to a sprint
        for (UserStory us : userStories) {
            if (us.getSprintBackLog() != null) {
                throw new ValidationException(
                    "User Story with id " + us.getId() +
                    " is already assigned to a Sprint");
            }
        }
    }
}
