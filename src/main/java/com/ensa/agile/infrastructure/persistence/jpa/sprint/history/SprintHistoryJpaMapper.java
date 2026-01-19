package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;

public class SprintHistoryJpaMapper {
    public static SprintHistoryJpaEntity toJpaEntity(SprintHistory domain) {
        if (domain == null) {
            return null;
        }
        return SprintHistoryJpaEntity.builder()
            .id(domain.getId())
            .sprint(domain.getSprint() == null ? null :
            SprintBackLogJpaMapper.toJpaEntity(domain.getSprint()))
            .status(domain.getStatus())
            .note(domain.getNote())
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    public static SprintHistory
    toDomainEntity(SprintHistoryJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return SprintHistory.builder()
            .id(jpaEntity.getId())
            .sprint(jpaEntity.getSprint() == null ? null :
                SprintBackLogJpaMapper.toDomainEntity(jpaEntity.getSprint()))
            .status(jpaEntity.getStatus())
            .note(jpaEntity.getNote())
            .createdBy(jpaEntity.getCreatedBy())
            .createdDate(jpaEntity.getCreatedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .build();
    }
}
