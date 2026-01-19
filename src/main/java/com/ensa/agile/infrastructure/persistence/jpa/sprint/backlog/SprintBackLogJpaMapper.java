package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.history.SprintHistoryJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;

public class SprintBackLogJpaMapper {
    public static SprintBackLog
    toDomainEntity(SprintBackLogJpaEntity sprintBacklogJpaEntity) {
        if (sprintBacklogJpaEntity == null) {
            return null;
        }
        return SprintBackLog.builder()
            .id(sprintBacklogJpaEntity.getId())
            .name(sprintBacklogJpaEntity.getName())
            .productBackLog(sprintBacklogJpaEntity.getProductBackLog() == null ? null :
            ProductBackLogJpaMapper.toDomainEntity(
                sprintBacklogJpaEntity.getProductBackLog()))
            .scrumMaster(sprintBacklogJpaEntity.getScrumMaster() == null ? null :
            UserJpaMapper.toDomainEntity(
                sprintBacklogJpaEntity.getScrumMaster()))
            .status(sprintBacklogJpaEntity.getStatus() == null ? null :
            SprintHistoryJpaMapper.toDomainEntity(
                sprintBacklogJpaEntity.getStatus()))
            .startDate(sprintBacklogJpaEntity.getStartDate())
            .endDate(sprintBacklogJpaEntity.getEndDate())
            .goal(sprintBacklogJpaEntity.getGoal())
            .createdDate(sprintBacklogJpaEntity.getCreatedDate())
            .createdBy(sprintBacklogJpaEntity.getCreatedBy())
            .lastModifiedDate(sprintBacklogJpaEntity.getLastModifiedDate())
            .lastModifiedBy(sprintBacklogJpaEntity.getLastModifiedBy())
            .build();
    }

    public static SprintBackLogJpaEntity
    toJpaEntity(SprintBackLog sprintBackLog) {
        if (sprintBackLog == null) {
            return null;
        }
        return SprintBackLogJpaEntity.builder()
            .id(sprintBackLog.getId())
            .name(sprintBackLog.getName())
            .productBackLog(sprintBackLog.getProductBackLog() == null ? null :
            ProductBackLogJpaMapper.toJpaEntity(
                sprintBackLog.getProductBackLog()))
            .scrumMaster(sprintBackLog.getScrumMaster() == null ? null :
                UserJpaMapper.toJpaEntity(sprintBackLog.getScrumMaster()))
            .status(sprintBackLog.getStatus() == null ? null :
            SprintHistoryJpaMapper.toJpaEntity(
                sprintBackLog.getStatus()))
            .startDate(sprintBackLog.getStartDate())
            .endDate(sprintBackLog.getEndDate())
            .goal(sprintBackLog.getGoal())
            .createdDate(sprintBackLog.getCreatedDate())
            .createdBy(sprintBackLog.getCreatedBy())
            .lastModifiedDate(sprintBackLog.getLastModifiedDate())
            .lastModifiedBy(sprintBackLog.getLastModifiedBy())
            .build();
    }
}
