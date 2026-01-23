package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.TaskUpdateRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateTaskUseCase
    extends BaseUseCase<TaskUpdateRequest, TaskResponse> {

    private final TaskRepository taskRepository;

    public UpdateTaskUseCase(ITransactionalWrapper tr,
                             TaskRepository taskRepository) {
        super(tr);
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse execute(TaskUpdateRequest request) {
        Task task = this.taskRepository.findById(request.getId());

        task.updateMetadata(request.getTitle(), request.getDescription(),
                            request.getEstimatedHours(),
                            request.getActualHours()

        );

        task = this.taskRepository.save(task);

        return TaskResponseMapper.toResponse(task);
    }
}
