package com.ensa.agile.application.task.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.repository.TaskHistoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;

@Component
public class UpdateTaskUseCase
    extends BaseUseCase<TaskUpdateRequest, TaskResponse> {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintBackLogRepository sprintRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public UpdateTaskUseCase(ITransactionalWrapper tr,
                             TaskRepository taskRepository,
                             UserRepository userRepository,
                             UserStoryRepository userStoryRepository,
                             SprintBackLogRepository sprintRepository,
                             TaskHistoryRepository taskHistoryRepository) {
        super(tr);
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userStoryRepository = userStoryRepository;
        this.sprintRepository = sprintRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    @Override
    public TaskResponse execute(TaskUpdateRequest request) {
        Task task = this.taskRepository.findById(request.getId());
        User user = null;
        UserStory userStory = null;
        SprintBackLog sprint = null;

        task.updateMetadata(request.getTitle(), request.getDescription(),
                            request.getEstimatedHours(),
                            request.getActualHours()

        );

        if (request.getAssigneeEmail() != null) {
            user = this.userRepository.findByEmail(request.getAssigneeEmail());
            task.setAssignee(user);
        }
        if (request.getUserStoryId() != null) {
            userStory =
                this.userStoryRepository.findById(request.getUserStoryId());
            task.setUserStory(userStory);
        }
        if (request.getSprintId() != null) {
            sprint = this.sprintRepository.findById(request.getSprintId());
            task.setSprintBackLog(sprint);
        }

        task = this.taskRepository.save(task);

        // here i need to check if status can go from current status to the new
        // status
        TaskHistory status = null;
        if (request.getStatus() != null) {
            status = this.taskHistoryRepository.save(
                TaskHistory.builder()
                    .task(task)
                    .note("Task updated with title: " + request.getTitle())
                    .status(request.getStatus())
                    .build());
        }

        return TaskResponseMapper.toResponse(
            task, user != null ? user.getEmail() : null, status);
    }
}
