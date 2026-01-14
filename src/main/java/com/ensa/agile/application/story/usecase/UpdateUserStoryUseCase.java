package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.application.story.request.UserStoryUpdateRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

public class UpdateUserStoryUseCase
    extends BaseUseCase<UserStoryUpdateRequest, UserStoryResponse> {
    private UserStoryRepository userStoryRepository;
    private ProductBackLogRepository productBackLogRepository;
    private EpicRepository epicRepository;
    private SprintBackLogRepository sprintBackLogRepository;

    public UpdateUserStoryUseCase(
        ITransactionalWrapper tr, UserStoryRepository userStoryRepository,
        ProductBackLogRepository productBackLogRepository,
        EpicRepository epicRepository,
        SprintBackLogRepository sprintBackLogRepository) {

        super(tr);
        this.userStoryRepository = userStoryRepository;
        this.productBackLogRepository = productBackLogRepository;
        this.epicRepository = epicRepository;
        this.sprintBackLogRepository = sprintBackLogRepository;
    }

    @Override
    public UserStoryResponse execute(UserStoryUpdateRequest request) {
        UserStory us = this.userStoryRepository.findById(request.getId());

        // i think this will not work because the how JPA queries work
        if (us.getProductBackLog() == null ||
            us.getProductBackLog().getId() == null ||
            !this.productBackLogRepository.existsById(
                us.getProductBackLog().getId())) {
            throw new ProductBackLogNotFoundException();
        }

        Epic epic = null;
        SprintBackLog sprint = null;

        if (request.getEpicId() != null) {
            epic = this.epicRepository.findById(request.getEpicId());
            us.setEpic(epic);
        }

        if (request.getSprintId() != null) {
            sprint =
                this.sprintBackLogRepository.findById(request.getSprintId());
            us.setSprintBackLog(sprint);
        }

        us.updateMetaData(request.getTitle(), request.getDescription(),
                          request.getPriority(), request.getStoryPoints(),
                          request.getAcceptanceCriteria());

        UserStory newUserStory = this.userStoryRepository.save(us);

        return UserStoryResponseMapper.toResponse(newUserStory);
    }
}
