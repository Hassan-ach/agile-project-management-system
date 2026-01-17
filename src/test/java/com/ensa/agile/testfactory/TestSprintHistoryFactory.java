package com.ensa.agile.testfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.history.SprintHistoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public class TestSprintHistoryFactory {

    public static SprintHistoryJpaEntity validSprintHistoryJpaEntity(SprintBackLogJpaEntity sb, SprintStatus status, UserJpaEntity logedUser) {
        return SprintHistoryJpaEntity.builder()
            .sprint(sb)
            .status(status)
            .note("Valid Sprint History Note")
            .createdBy(logedUser.getId())
            .createdDate(LocalDateTime.now())
            .build();
    }

    public static List<SprintHistoryJpaEntity> multipleSprintHistoryJpaEntities(SprintBackLogJpaEntity sb, SprintStatus status, UserJpaEntity logedUser, Integer count) {
        return java.util.stream.IntStream.range(0, count)
            .mapToObj(i -> validSprintHistoryJpaEntity(sb, status, logedUser))
            .toList();
    }
}
