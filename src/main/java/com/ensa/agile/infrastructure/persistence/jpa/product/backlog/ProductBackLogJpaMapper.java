package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductBackLogJpaMapper {

    public static ProductBackLogJpaEntity
    toJpaEntity(ProductBackLog productBackLog) {
        if (productBackLog == null) {
            return null;
        }

        ProductBackLogJpaEntity jpaEntity =
            ProductBackLogJpaEntity.builder()
                .id(productBackLog.getId())
                .name(productBackLog.getName())
                .description(productBackLog.getDescription())
                .createdDate(productBackLog.getCreatedDate())
                .createdBy(productBackLog.getCreatedBy())
                .lastModifiedDate(productBackLog.getLastModifiedDate())
                .lastModifiedBy(productBackLog.getLastModifiedBy())
                .build();
        return jpaEntity;
    }

    public static ProductBackLog
    toDomainEntity(ProductBackLogJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return ProductBackLog.builder()
            .id(jpaEntity.getId())
            .name(jpaEntity.getName())
            .description(jpaEntity.getDescription())
            .createdDate(jpaEntity.getCreatedDate())
            .createdBy(jpaEntity.getCreatedBy())
            .lastModifiedDate(jpaEntity.getLastModifiedDate())
            .lastModifiedBy(jpaEntity.getLastModifiedBy())
            .build();
    }

    public static ProductBackLog toDomainEntity(List<ProductBackLogRow> rows) {
        if (rows == null || rows.isEmpty()) {
            return ProductBackLog.builder().build();
        }

        ProductBackLogRow row0 = rows.get(0);
        ProductBackLog resp = ProductBackLog.builder()
                                  .id(row0.getBacklogId())
                                  .name(row0.getBacklogName())
                                  .description(row0.getBacklogDescription())
                                  .projectMembers(new ArrayList<>())
                                  .epics(new ArrayList<>())
                                  .userStories(new ArrayList<>())
                                  .sprintBackLogs(new ArrayList<>())
                                  .build();

        HashMap<String, Epic> epics = new HashMap<String, Epic>();
        List<UserStory> userStories = new ArrayList<>();

        for (ProductBackLogRow row : rows) {
            if (row.getEpicId() != null) {
                epics.computeIfAbsent(
                    row.getEpicId(),
                    id
                    -> Epic.builder()
                           .id(row.getEpicId())
                           .title(row.getEpicTitle())
                           .description(row.getEpicDescription())
                           .userStories(new ArrayList<>())
                           .build());
            }

            if (row.getStoryId() == null)
                continue;

            UserStory us = UserStory.builder()
                               .id(row.getStoryId())
                               .title(row.getStoryTitle())
                               .description(row.getStoryDescription())
                               .priority(row.getPriority())
                               .storyPoints(row.getStoryPoints())
                               .acceptanceCriteria(row.getAcceptanceCriteria())
                               .build();

            if (row.getEpicId() == null) {
                userStories.add(us);
            } else {
                epics.get(row.getEpicId()).getUserStories().add(us);
            }
        }

        resp.setUserStories(userStories);
        resp.setEpics(epics.values().stream().toList());

        return resp;
    }
}
