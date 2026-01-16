package com.ensa.agile.application.sprint.usecase;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateSprintBackLogUseCaseTest {

    @Mock private ITransactionalWrapper transactionalWrapper;
    @Mock private SprintBackLogRepository sprintBackLogRepository;
    @Mock private ProductBackLogRepository productBackLogRepository;
    @Mock private UserStoryRepository userStoryRepository;
    @Mock private SprintHistoryRepository sprintHistoryRepository;

    @InjectMocks private CreateSprintBackLogUseCase createSprintBackLogUseCase;

    @Test
    void execute_ShouldCreateSprintBackLog_WhenDataIsValid() {
        // Test implementation goes here
    }

    @Test
    void execute_ShouldThrowException_WhenProductBackLogNotFound() {
        // Test implementation goes here
    }


}
