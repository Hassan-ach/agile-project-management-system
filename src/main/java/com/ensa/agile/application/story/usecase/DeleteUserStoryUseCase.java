package com.ensa.agile.application.story.usecase;

import com.ensa.agile.application.common.response.DeleteResponse;
import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserStoryUseCase extends BaseUseCase<UUID, DeleteResponse> {
    private UserStoryRepository userStoryRepository;
    public DeleteUserStoryUseCase(ITransactionalWrapper tr,
                                  UserStoryRepository userStoryRepository) {
        super(tr);
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public DeleteResponse execute(UUID id) {
        if (!userStoryRepository.existsById(id)) {
            throw new UserStoryNotFoundException();
        }

        this.userStoryRepository.deleteById(id);
        return DeleteResponse.builder()
            .success(true)
            .message("User Story deleted successfully")
            .build();
    }
}
