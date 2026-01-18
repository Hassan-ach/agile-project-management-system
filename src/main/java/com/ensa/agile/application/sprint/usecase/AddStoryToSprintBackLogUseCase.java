package com.ensa.agile.application.sprint.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.mapper.SprintBackLogResponseMapper;
import com.ensa.agile.application.sprint.request.UpdateStorySprintRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

@Component
public class AddStoryToSprintBackLogUseCase extends BaseUseCase<UpdateStorySprintRequest, SprintBackLogResponse> {

    private final SprintBackLogRepository sprintBackLogRepository;
    private final UserStoryRepository userStoryRepository;

    public AddStoryToSprintBackLogUseCase(
        SprintBackLogRepository sprintBackLogRepository,
        UserStoryRepository userStoryRepository,
        ITransactionalWrapper transactionalWrapper) {
        super(transactionalWrapper);
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.userStoryRepository = userStoryRepository;
    }
    @Override
    public SprintBackLogResponse execute(UpdateStorySprintRequest data) {
        SprintBackLog sp = sprintBackLogRepository.findById(data.getId());
        UserStory story = userStoryRepository.findById(data.getStoryId());

        story.linkSprint(sp);

        story = userStoryRepository.save(story);
        
        return SprintBackLogResponseMapper.toResponse(sp);
    }

    
}
