package com.ensa.agile.infrastructure.persistence.jpa.global.transaction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ensa.agile.application.global.transaction.ITransactionCallBack;
import com.ensa.agile.domain.global.exception.DataBaseTransactionException;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.JpaProductBackLogRepository;
import com.ensa.agile.infrastructure.persistence.jpa.product.member.JpaProjectMemberRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestProjectMemberFactory;
import com.ensa.agile.testfactory.TestUserFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TransactionWrapper.class)
class TransactionWrapperTest {

    @Autowired
    private TransactionWrapper tWrapper;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaProductBackLogRepository productBackLogRepository;

    @Autowired
    private JpaProjectMemberRepository projectMemberRepository;

    private UserJpaEntity user;
    
    @BeforeEach
    void setUp(){
        user = userRepository.save(TestUserFactory.validJpaUser());
    }

    @Test
    //* This because of how psql behaves with transactions 
    // - in postgresql, if one query failed it marks the intire transaction as "Aborted"
    // - in @DataJpaTest each test is wrapped in a transaction that is rolled back at the end of the test
    // - so if we try to execute a query after a failure in the same transaction it will always fail
    // - to avoid this we need to annotate the test with @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void execute_shouldRollbackIfAndThrowExceptionIfAnyExceptionAccured(){

        Runnable func = ( ) -> {
            productBackLogRepository.saveAndFlush(TestProductBackLogFactory.validJpaProduct(user));
            assertTrue(productBackLogRepository.findAll().size() == 1);

            projectMemberRepository.saveAndFlush(TestProjectMemberFactory.validJpaProjectMember(RoleType.PRODUCT_OWNER, MemberStatus.ACTIVE,null, user, user));
        };


        assertThrows(DataBaseTransactionException.class, ()->
            tWrapper.execute(
                new ITransactionCallBack<Void>() {
                    public Void execution(){
                        func.run();
                        return null;
                    }
                }
            )
        );

        assertTrue(productBackLogRepository.findAll().isEmpty());
    }
}
