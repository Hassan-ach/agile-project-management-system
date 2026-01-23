package com.ensa.agile.infrastructure.persistence.jpa.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.epic.JpaEpicRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.JpaSprintBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.history.JpaUserStoryHistoryRepository;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.JpaUserStoryRepository;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.testfactory.TestEpicFactory;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import com.ensa.agile.testfactory.TestUserStoryFactory;
import com.ensa.agile.testfactory.TestUserStoryHistoryFactory;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class JpaUserStoryRepositoryTest {

    @Autowired private JpaUserStoryRepository userStoryRepository;

    @Autowired private JpaUserStoryHistoryRepository userStoryHistoryRepository;

    @Autowired private JpaEpicRepository epicRepository;

    @Autowired private JpaSprintBackLogRepository sprintBackLogRepository;

    @Autowired private JpaProductBackLogRepository productBackLogRepository;

    @Autowired private JpaUserRepository userRepository;

    private EpicJpaEntity epic;
    private ProductBackLogJpaEntity product;
    private SprintBackLogJpaEntity sprint;
    private UserStoryJpaEntity userStory;

    @BeforeEach
    void setUp() {
        var owner = userRepository.save(TestUserFactory.validJpaUser());

        product = productBackLogRepository.save(
            TestProductBackLogFactory.validJpaProduct(owner));

        epic =
            epicRepository.save(TestEpicFactory.validJpaEpic(product, owner));

        sprint = sprintBackLogRepository.save(
            TestSprintBackLogFactory.validJpaSprint(product, owner, owner));

        userStory = userStoryRepository.save(
            TestUserStoryFactory.validJpaUserStoryWithEpic(product, epic,
                                                           owner));

        userStoryHistoryRepository.save(
            TestUserStoryHistoryFactory.validUserStoryHistoryJpaEntity(
                userStory, StoryStatus.TODO, owner));
    }

    @Test
    void findAllByEpicId_shouldReturnUserStories() {
        List<UserStoryJpaEntity> stories =
            userStoryRepository.findAllByEpic_Id(epic.getId());
        assertEquals(1, stories.size());
        assertEquals(userStory.getId(), stories.get(0).getId());
    }

    @Test
    void findByBatch_shouldReturnMatchingUserStories() {
        List<UserStoryJpaEntity> stories =
            userStoryRepository.findByBatch(List.of(userStory.getId()));
        assertEquals(1, stories.size());
        assertEquals(userStory.getId(), stories.get(0).getId());
    }

    @Test
    void assignToSprint_shouldUpdateSprintBackLog() {
        userStoryRepository.assignToSprint(List.of(userStory.getId()), sprint);

        Optional<UUID> sprintIdOpt =
            userStoryRepository.getSprintBackLogIdByUserStoryId(
                userStory.getId());
        assertTrue(sprintIdOpt.isPresent());
        assertEquals(sprint.getId(), sprintIdOpt.get());
    }

    @Test
    void getSprintBackLogIdByUserStoryId_shouldReturnOptional() {
        userStory.setSprintBackLog(sprint);
        userStoryRepository.save(userStory);

        Optional<UUID> sprintIdOpt =
            userStoryRepository.getSprintBackLogIdByUserStoryId(
                userStory.getId());
        assertTrue(sprintIdOpt.isPresent());
        assertEquals(sprint.getId(), sprintIdOpt.get());
    }

    @Test
    void getProductBackLogIdByUserStoryId_shouldReturnOptional() {
        Optional<UUID> productIdOpt =
            userStoryRepository.getProductBackLogIdByUserStoryId(
                userStory.getId());
        assertTrue(productIdOpt.isPresent());
        assertEquals(product.getId(), productIdOpt.get());
    }
}
