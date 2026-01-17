package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.history.JpaSprintHistoryRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestSprintHistoryFactory;
import com.ensa.agile.testfactory.TestUserFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaSprintBackLogRepositoryTest {

    @Autowired
    private JpaSprintBackLogRepository sprintBackLogRepository;

    @Autowired
    private JpaSprintHistoryRepository sprintHistoryRepository;
    
    @Autowired
    private JpaProductBackLogRepository productBackLogRepository;

    @Autowired
    private JpaUserRepository userRepository;

    private UserJpaEntity owner;
    private UserJpaEntity scrumMaster;
    private ProductBackLogJpaEntity product;
    private SprintBackLogJpaEntity sprint;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(TestUserFactory.validJpaUser());
        scrumMaster = userRepository.save(TestUserFactory.validJpaUser());

        product = productBackLogRepository.save(
            TestProductBackLogFactory.validJpaProduct(owner)
        );

        sprint = sprintBackLogRepository.save(
            TestSprintBackLogFactory.validJpaSprint(
                product,scrumMaster,owner
            )
        );

        sprintHistoryRepository.save(
            TestSprintHistoryFactory.validSprintHistoryJpaEntity(sprint, SprintStatus.PLANNED, owner)
        );
    }


    @Test
    void getProductBackLogIdBySprintId_shouldReturnProductBackLogId() {
        UUID productBackLogId =
            sprintBackLogRepository.getProductBackLogIdBySprintId(sprint.getId());

        assertEquals(product.getId(), productBackLogId);
    }
}

