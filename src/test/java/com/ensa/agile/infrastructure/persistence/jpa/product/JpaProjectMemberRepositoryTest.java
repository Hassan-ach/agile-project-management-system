package com.ensa.agile.infrastructure.persistence.jpa.product;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.JpaProjectMemberRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestProjectMemberFactory;
import com.ensa.agile.testfactory.TestUserFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProjectMemberRepositoryTest {

    @Autowired
    private JpaProductBackLogRepository productBackLogRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaProjectMemberRepository projectMemberRepository;

    private UserJpaEntity loggedUser;
    private UserJpaEntity member;
    private ProductBackLogJpaEntity product;

    @BeforeEach
    void setUp() {
        loggedUser = userRepository.save(TestUserFactory.validJpaUser());
        member = userRepository.save(TestUserFactory.validJpaUser());

        product = productBackLogRepository.save(
            TestProductBackLogFactory.validJpaProduct(loggedUser)
        );

        projectMemberRepository.save(
            TestProjectMemberFactory.validJpaProjectMember(
                RoleType.PRODUCT_OWNER,
                MemberStatus.ACTIVE,
                product,
                loggedUser,
                loggedUser
            )
        );
        projectMemberRepository.save(
            TestProjectMemberFactory.validJpaProjectMember(
                RoleType.SCRUM_MASTER,
                MemberStatus.ACTIVE,
                product,
                member,
                loggedUser
            )
        );
    }

    @Test
    void existsByUserIdAndProductId_shouldReturnTrue() {
        assertTrue(
            projectMemberRepository.existsByUser_IdAndProductBackLog_Id(
                member.getId(),
                product.getId()
            )
        );
    }

    @Test
    void existsByUserEmailAndProductId_shouldReturnTrue() {
        assertTrue(
            projectMemberRepository.existsByUser_EmailAndProductBackLog_Id(
                member.getEmail(),
                product.getId()
            )
        );
    }

    @Test
    void existsByUserIdProductIdAndRole_scrumMaster_shouldReturnTrue() {
        assertTrue(
            projectMemberRepository.existsByUser_IdAndProductBackLog_IdAndRole(
                member.getId(),
                product.getId(),
                RoleType.SCRUM_MASTER
            )
        );
    }

    @Test
    void existsByUserIdProductIdAndRole_productOwner_shouldReturnTrue() {
        assertTrue(
            projectMemberRepository.existsByUser_IdAndProductBackLog_IdAndRole(
                loggedUser.getId(),
                product.getId(),
                RoleType.PRODUCT_OWNER
            )
        );
    }

    @Test
    void existsByUserEmailProductIdAndRole_productOwner_shouldReturnTrue() {
        assertTrue(
            projectMemberRepository.existsByUser_EmailAndProductBackLog_IdAndRole(
                loggedUser.getEmail(),
                product.getId(),
                RoleType.PRODUCT_OWNER
            )
        );
    }
}

