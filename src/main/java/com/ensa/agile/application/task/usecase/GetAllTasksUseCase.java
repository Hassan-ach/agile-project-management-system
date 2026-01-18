package com.ensa.agile.application.task.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;

@Component
public class GetAllTasksUseCase extends BaseUseCase<UUID, List<TaskResponse>> {

    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;

    public GetAllTasksUseCase(ITransactionalWrapper tr,
                              TaskRepository taskRepository,
                              UserStoryRepository userStoryRepository) {
        super(tr);
        this.taskRepository = taskRepository;
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public List<TaskResponse> execute(UUID userStoryId) {
        if (!userStoryRepository.existsById(userStoryId)) {
            throw new UserStoryNotFoundException();
        }
        return taskRepository.findAllByUserStoryId(userStoryId)
                .stream()
                .map(TaskResponseMapper::toResponse)
                .toList();
    }

    
}
