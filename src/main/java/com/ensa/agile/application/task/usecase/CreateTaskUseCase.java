package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.TaskCreateRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.domain.task.repository.TaskHistoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskUseCase
    extends BaseUseCase<TaskCreateRequest, TaskResponse> {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintBackLogRepository sprintBackLogRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public CreateTaskUseCase(ITransactionalWrapper tr,
                             TaskRepository taskRepository,
                             UserRepository userRepository,
                             SprintBackLogRepository sprintBackLogRepository,
                             UserStoryRepository userStoryRepository,
                             TaskHistoryRepository taskHistoryRepository) {
        super(tr);
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.userStoryRepository = userStoryRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    @Override
    public TaskResponse execute(TaskCreateRequest request) {
        User ur = this.userRepository.findByEmail(request.getAssigneeEmail());
        UserStory userStory =
            this.userStoryRepository.findById(request.getUserStoryId());
        SprintBackLog sp =
            this.sprintBackLogRepository.findById(request.getSprintId());

        Task task = this.taskRepository.save(
            Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .estimatedHours(request.getEstimatedHours())
                .assignee(ur)
                .userStory(userStory)
                .sprintBackLog(sp)
                .build());

        TaskHistory status = this.taskHistoryRepository.save(
            TaskHistory.builder()
                .task(task)
                .status(TaskStatus.NEW)
                .note("Task created with title: " + request.getTitle())
                .build());

        task.setStatus(status);

        return TaskResponseMapper.toResponse(task, ur.getEmail());
    }
}
