package com.ensa.agile.application.task.usecase;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.task.repository.TaskRepository;

@Component
public class DeleteTaskUseCase extends BaseUseCase<String, DeleteResponse> {
    private final TaskRepository taskRepository;

    public DeleteTaskUseCase(ITransactionalWrapper tr,
                             TaskRepository taskRepository) {
        super(tr);
        this.taskRepository = taskRepository;
    }

    @Override
    public DeleteResponse execute(String id) {
        this.taskRepository.deleteById(id);
        return DeleteResponse.builder()
            .message("Task deleted successfully")
            .success(true)
            .build();
    }
}
