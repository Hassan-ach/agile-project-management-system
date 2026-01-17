package com.ensa.agile.infrastructure.persistence.jpa.task.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.JpaSprintBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.JpaUserStoryRepository;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestTaskFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import com.ensa.agile.testfactory.TestUserStoryFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaTaskRepositoryTest {

    @Autowired
    private JpaTaskRepository taskRepository;

    @Autowired
    private JpaUserStoryRepository userStoryRepository;

    @Autowired
    private JpaSprintBackLogRepository sprintBackLogRepository;

    @Autowired
    private JpaProductBackLogRepository productBackLogRepository;

    @Autowired
    private JpaUserRepository userRepository;

    private ProductBackLogJpaEntity product;
    private SprintBackLogJpaEntity sprint;
    private UserStoryJpaEntity userStory;
    private TaskJpaEntity task;

    @BeforeEach
    void setUp() {
        var owner = userRepository.save(TestUserFactory.validJpaUser());

        product = productBackLogRepository.save(
            TestProductBackLogFactory.validJpaProduct(owner)
        );

        sprint = sprintBackLogRepository.save(
            TestSprintBackLogFactory.validJpaSprint(product, owner, owner)
        );

        userStory = userStoryRepository.save(
            TestUserStoryFactory.validJpaUserStory( product, owner) // epic can be null if not used
        );

        // assign to sprint
        userStory.setSprintBackLog(sprint);
        userStoryRepository.save(userStory);

        task = taskRepository.save(
            TestTaskFactory.validJpaTask(sprint, userStory, owner)
        );
    }

    @Test
    void getProductBackLogIdByTaskId_shouldReturnCorrectProductBackLogId() {
        Optional<UUID> productIdOpt = taskRepository.getProductBackLogIdByTaskId(task.getId());

        assertTrue(productIdOpt.isPresent());
        assertEquals(product.getId(), productIdOpt.get());
    }

    @Test
    void getProductBackLogIdByTaskId_shouldReturnEmptyForUnknownTask() {
        Optional<UUID> productIdOpt = taskRepository.getProductBackLogIdByTaskId(UUID.randomUUID());

        assertFalse(productIdOpt.isPresent());
    }
}

