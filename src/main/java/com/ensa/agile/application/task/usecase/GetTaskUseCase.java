package com.ensa.agile.application.task.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.task.exception.TaskNotFoundException;
import com.ensa.agile.application.task.mapper.TaskResponseMapper;
import com.ensa.agile.application.task.request.TaskGetRequest;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.task.repository.TaskRepository;

@Component
public class GetTaskUseCase extends BaseUseCase<TaskGetRequest, TaskResponse> {
    private final TaskRepository taskRepository;
    public GetTaskUseCase(ITransactionalWrapper tr,
                          TaskRepository taskRepository) {
        super(tr);
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse execute(TaskGetRequest request) {
        if (!taskRepository.existsById(request.getId())) {
            throw new TaskNotFoundException();
        }

        return TaskResponseMapper.toResponse(
            this.taskRepository.findById(request.getId()));
    }
}
