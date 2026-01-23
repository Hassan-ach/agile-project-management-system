package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.UpdateAssignTaskRequest;
import com.ensa.agile.application.task.response.UpdateAssignTaskResponse;
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
    extends BaseUseCase<UpdateAssignTaskRequest, UpdateAssignTaskResponse> {

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

    public UpdateAssignTaskResponse execute(UpdateAssignTaskRequest request) {
        User assignee =
            this.userRepository.findByEmail(request.getAssigneeEmail());
        Task task = this.taskRepository.findById(request.getId());

        // check if the assignee is a member of the sprint
        if (!sprintMembersRepository.existsBySprintBackLogIdAndUserId(
                taskRepository.getSprintIdByTaskId(request.getId()),
                assignee.getId())) {

            return UpdateAssignTaskResponse.builder()
                .task(null)
                .assigned(false)
                .message("User " + assignee.getEmail() +
                         " is not a member of the sprint")
                .build();
        }

        // check if the task is already assigned
        if (task.getAssignee() != null) {
            return UpdateAssignTaskResponse.builder()
                .task(null)
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

        return UpdateAssignTaskResponse.builder()
            .task(TaskResponseMapper.toResponse(
                newTask, TaskResponseMapper::attachAssignee))
            .assigned(true)
            .message("Task assigned to " + assignee.getEmail())
            .build();
    }
}
