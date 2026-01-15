package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.common.request.UpdateStatusRequest;
import com.ensa.agile.application.common.response.UpdateStatusResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.domain.task.repository.TaskHistoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateTaskStatusUseCase
    extends BaseUseCase<UpdateStatusRequest<TaskStatus>,
                        UpdateStatusResponse<TaskHistory>> {
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskRepository taskRepository;

    public UpdateTaskStatusUseCase(ITransactionalWrapper tr,
                                   TaskHistoryRepository taskHistoryRepository,
                                   TaskRepository taskRepository) {
        super(tr);
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public UpdateStatusResponse<TaskHistory>
    execute(UpdateStatusRequest<TaskStatus> request) {

        Task task = this.taskRepository.findById(request.getId());
        TaskHistory status = task.getStatus();

        // check if the new status is BLOCKED, if not validate the transition
        if (request.getStatus() != TaskStatus.BLOCKED) {
            if (status.getStatus() == request.getStatus()) {
                return UpdateStatusResponse.<TaskHistory>builder()
                    .status(null)
                    .updated(false)
                    .message("Status is already " + request.getStatus().name())
                    .build();
            }
            TaskStatus nextStatus = status.getNextStatus();
            if (nextStatus != request.getStatus()) {
                return UpdateStatusResponse.<TaskHistory>builder()
                    .status(null)
                    .updated(false)
                    .message("Invalid status transition from " +
                             status.getStatus().name() + " to " +
                             request.getStatus().name())
                    .build();
            }
        }

        TaskHistory newStatus = TaskHistory.builder()
                                    .task(task)
                                    .status(request.getStatus())
                                    .note(request.getNote())
                                    .build();

        return UpdateStatusResponse.<TaskHistory>builder()
            .status(this.taskHistoryRepository.save(newStatus))
            .updated(true)
            .message("Task status updated successfully to " +
                     request.getStatus().name())
            .build();
    }
}
