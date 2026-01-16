package com.ensa.agile.application.task.usecase;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.domain.task.repository.TaskRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DeleteTaskUseCase extends BaseUseCase<UUID, DeleteResponse> {
    private final TaskRepository taskRepository;

    public DeleteTaskUseCase(ITransactionalWrapper tr,
                             TaskRepository taskRepository) {
        super(tr);
        this.taskRepository = taskRepository;
    }

    @Override
    public DeleteResponse execute(UUID id) {
        this.taskRepository.deleteById(id);
        return DeleteResponse.builder()
            .message("Task deleted successfully")
            .success(true)
            .build();
    }
}
