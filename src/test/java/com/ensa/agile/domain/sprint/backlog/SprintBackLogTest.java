package com.ensa.agile.domain.sprint.backlog;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.testfactory.TestProductBackLogFactory;
import com.ensa.agile.testfactory.TestUserFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class SprintBackLogTest {

    @Test
    void shouldCreateSprintBacklog_whenAllFieldsValid() {
        ProductBackLog product = TestProductBackLogFactory.validProduct();
        User scrumMaster = TestUserFactory.validUser();

        assertDoesNotThrow(()
                               -> SprintBackLog.builder()
                                      .name("Sprint 1")
                                      .goal("Deliver core features")
                                      .productBackLog(product)
                                      .scrumMaster(scrumMaster)
                                      .startDate(LocalDate.now())
                                      .endDate(LocalDate.now().plusWeeks(2))
                                      .build());
    }

    @Test
    void shouldFail_whenNameIsNull() {
        ProductBackLog product = TestProductBackLogFactory.validProduct();
        User scrumMaster = TestUserFactory.validUser();

        assertThrows(ValidationException.class,
                     ()
                         -> SprintBackLog.builder()
                                .goal("goal")
                                .productBackLog(product)
                                .scrumMaster(scrumMaster)
                                .startDate(LocalDate.now())
                                .endDate(LocalDate.now().plusWeeks(2))
                                .build());
    }

    @Test
    void shouldFail_whenStartDateAfterEndDate() {
        ProductBackLog product = TestProductBackLogFactory.validProduct();
        User scrumMaster = TestUserFactory.validUser();

        assertThrows(ValidationException.class,
                     ()
                         -> SprintBackLog.builder()
                                .name("Sprint 1")
                                .goal("goal")
                                .productBackLog(product)
                                .scrumMaster(scrumMaster)
                                .startDate(LocalDate.now().plusWeeks(3))
                                .endDate(LocalDate.now().plusWeeks(2))
                                .build());
    }
}
