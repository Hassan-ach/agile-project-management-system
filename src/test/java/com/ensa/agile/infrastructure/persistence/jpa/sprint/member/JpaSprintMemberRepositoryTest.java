package com.ensa.agile.infrastructure.persistence.jpa.sprint.member;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.JpaSprintBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestSprintBackLogFactory;
import com.ensa.agile.testfactory.TestSprintMemberFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class JpaSprintMemberRepositoryTest {

    @Autowired private JpaSprintMemberRepository sprintMemberRepository;

    @Autowired private JpaSprintBackLogRepository sprintBackLogRepository;

    @Autowired private JpaProductBackLogRepository productBackLogJpaEntity;

    @Autowired private JpaUserRepository userRepository;

    private UserJpaEntity user;
    private UserJpaEntity member;
    private SprintBackLogJpaEntity sprint;

    @BeforeEach
    void setUp() {
        user = userRepository.save(TestUserFactory.validJpaUser());
        member = userRepository.save(TestUserFactory.validJpaUser());

        var product = TestProductBackLogFactory.validJpaProduct(user);

        product = productBackLogJpaEntity.save(product);

        sprint = sprintBackLogRepository.save(
            TestSprintBackLogFactory.validJpaSprint(product, user, member));

        sprintMemberRepository.save(
            TestSprintMemberFactory.validJpaSprintMember(sprint, member, user));
    }

    @Test
    void existsBySprintIdAndUserId_shouldReturnTrue_whenMemberExists() {
        assertTrue(sprintMemberRepository.existsBySprintBackLog_IdAndUser_Id(
            sprint.getId(), member.getId()));
    }

    @Test
    void existsBySprintIdAndUserId_shouldReturnFalse_whenMemberDoesNotExist() {
        UUID randomUserId = UUID.randomUUID();
        assertFalse(sprintMemberRepository.existsBySprintBackLog_IdAndUser_Id(
            sprint.getId(), randomUserId));
    }

    @Test
    void deleteByUserEmailAndSprintId_shouldRemoveMember() {
        sprintMemberRepository.deleteByUser_EmailAndSprintBackLog_Id(
            member.getEmail(), sprint.getId());

        assertFalse(sprintMemberRepository.existsBySprintBackLog_IdAndUser_Id(
            member.getId(), user.getId()));
    }
}
