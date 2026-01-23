package com.ensa.agile.infrastructure.persistence.jpa.epic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.testfactory.TestEpicFactory;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestUserFactory;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class JpaEpicRepositoryTest {

    @Autowired
    private JpaEpicRepository epicRepository;

    @Autowired
    private JpaProductBackLogRepository productBackLogRepository;

    @Autowired
    private JpaUserRepository userRepository;

    private ProductBackLogJpaEntity product;
    private EpicJpaEntity epic;

    @BeforeEach
    void setUp() {
        var owner = userRepository.save(TestUserFactory.validJpaUser());

        product = productBackLogRepository.save(
            TestProductBackLogFactory.validJpaProduct(owner)
        );

        epic = epicRepository.save(
            TestEpicFactory.validJpaEpic(product, owner)
        );
    }

    @Test
    void findAllByProductBackLogId_shouldReturnEpics() {
        List<EpicJpaEntity> epics = epicRepository.findAllByProductBackLog_Id(product.getId());
        assertEquals(1, epics.size());
        assertEquals(epic.getId(), epics.get(0).getId());
    }

    @Test
    void getProductBackLogIdByEpicId_shouldReturnCorrectProductBackLogId() {
        Optional<UUID> productIdOpt = epicRepository.getProductBackLogIdByEpicId(epic.getId());
        assertTrue(productIdOpt.isPresent());
        assertEquals(product.getId(), productIdOpt.get());
    }

    @Test
    void getProductBackLogIdByEpicId_shouldReturnEmptyForUnknownEpic() {
        Optional<UUID> productIdOpt = epicRepository.getProductBackLogIdByEpicId(UUID.randomUUID());
        assertFalse(productIdOpt.isPresent());
    }
}

