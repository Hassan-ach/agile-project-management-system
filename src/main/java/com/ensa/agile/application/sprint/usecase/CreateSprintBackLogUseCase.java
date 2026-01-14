package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintBacklogResponseMapper;
import com.ensa.agile.application.sprint.request.SprintBackLogCreateRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import java.util.List;

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

        // need to check if any of the user stories is already assigned to another sprint
        List<UserStory> userStories =
            this.userStoryRepository.findByBatch(request.getUserStoriesIds());

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
}
