package com.ensa.agile.application.task.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.UpdateAssignTaskRequest;
import com.ensa.agile.application.task.response.UpdateAssignTaskResponse;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.repository.TaskHistoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;

@Component
public class UnAssignTaskUseCase
    extends BaseUseCase<UpdateAssignTaskRequest, UpdateAssignTaskResponse> {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public UnAssignTaskUseCase(ITransactionalWrapper tr,
                             UserRepository userRepository,
                             TaskRepository taskRepository,
                             TaskHistoryRepository taskHistoryRepository) {
        super(tr);
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public UpdateAssignTaskResponse execute(UpdateAssignTaskRequest request) {
        User assignee =
            this.userRepository.findByEmail(request.getAssigneeEmail());

        Task task = this.taskRepository.findById(request.getId());

        task.unassignUser();

        Task newTask = this.taskRepository.save(task);

        TaskHistory status = this.taskHistoryRepository.save(
            TaskHistory.builder()
                .task(task)
                .status(task.getStatus().unassignStatus())
                .note("task unassigned to " + assignee.getEmail())
                .build());

        newTask.setStatus(status);

        return UpdateAssignTaskResponse.builder()
            .task(TaskResponseMapper.toResponse(newTask))
            .assigned(true)
            .message("Task unassigned to " + assignee.getEmail())
            .build();
    }
}
