package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.AssignTaskRequest;
import com.ensa.agile.application.task.response.AssignTaskResponse;
import com.ensa.agile.domain.sprint.repository.SprintMembersRepository;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.repository.TaskHistoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AssignTaskUseCase
    extends BaseUseCase<AssignTaskRequest, AssignTaskResponse> {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final SprintMembersRepository sprintMembersRepository;

    public AssignTaskUseCase(ITransactionalWrapper tr,
                             UserRepository userRepository,
                             TaskRepository taskRepository,
                             TaskHistoryRepository taskHistoryRepository,
                             SprintMembersRepository sprintMembersRepository) {
        super(tr);
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.sprintMembersRepository = sprintMembersRepository;
    }

    public AssignTaskResponse execute(AssignTaskRequest request) {
        User assignee =
            this.userRepository.findByEmail(request.getAssigneeEmail());
        Task task = this.taskRepository.findById(request.getId());

        // mybe this check is not necessary
        if (task.getUserStory() == null ||
            task.getUserStory().getId() == null ||
            !task.getUserStory().getId().equals(request.getUserStoryId())) {
            return AssignTaskResponse.builder()
                .task(null)
                .assigned(false)
                .message("Task does not belong to the specified user story")
                .build();
        }

        // check if the assignee is a member of the sprint
        if (!sprintMembersRepository.existsBySprintBackLogIdAndUserId(
                request.getSprintId(), assignee.getId())) {
            return AssignTaskResponse.builder()
                .task(null)
                .assigned(false)
                .message("User " + assignee.getEmail() +
                         " is not a member of the sprint")
                .build();
        }

        // check if the task is already assigned
        if (task.getAssignee() != null) {
            return AssignTaskResponse.builder()
                .task(TaskResponseMapper.toResponse(task))
                .assigned(false)
                .message("Task is already assigned to " +
                         task.getAssignee().getEmail())
                .build();
        }

        task.setAssignee(assignee);

        Task newTask = this.taskRepository.save(task);

        TaskHistory status = this.taskHistoryRepository.save(
            TaskHistory.builder()
                .task(task)
                .status(task.getStatus().getNextStatus())
                .note("task assigned to " + assignee.getEmail())
                .build());

        newTask.setStatus(status);

        return AssignTaskResponse.builder()
            .task(TaskResponseMapper.toResponse(newTask))
            .assigned(true)
            .message("Task assigned to " + assignee.getEmail())
            .build();
    }
}
