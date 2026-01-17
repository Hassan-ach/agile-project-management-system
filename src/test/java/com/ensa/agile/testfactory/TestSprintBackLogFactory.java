package com.ensa.agile.testfactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestSprintBackLogFactory {

    public static SprintBackLog validSprintBackLog() {
        return SprintBackLog.builder()
            .name("Valid Sprint Backlog")
            .goal("Valid Sprint Goal")
            .productBackLog(TestProductBackLogFactory.validProduct())
            .scrumMaster(TestUserFactory.validUser())
            .startDate(java.time.LocalDate.now())
            .endDate(java.time.LocalDate.now().plusWeeks(2))
            .build();
    }

    public static SprintBackLogJpaEntity validJpaSprint(
        ProductBackLogJpaEntity pb,
        UserJpaEntity scrumMaster,
        UserJpaEntity logedUser) {
        return SprintBackLogJpaEntity.builder()
            .name("Valid Sprint Backlog")
            .goal("Valid Sprint Goal")
            .productBackLog(pb)
            .scrumMaster(scrumMaster)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusWeeks(2))
            .createdBy(logedUser.getId())
            .createdDate(LocalDateTime.now())
            .build();
    }

    public List<SprintBackLogJpaEntity> multipleJpaSprintBackLogs(
        ProductBackLogJpaEntity pb,
        UserJpaEntity scrumMaster,
        UserJpaEntity logedUser,
        Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validJpaSprint(pb, scrumMaster, logedUser))
            .toList();
    }
}
