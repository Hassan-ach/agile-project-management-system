package com.ensa.agile.infrastructure.persistence.jpa.story.history;

import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaMapper;
import java.util.function.BiConsumer;

public class UserStoryHistoryJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserStoryHistory toDomain(UserStoryHistoryJpaEntity entity) {
        if (entity == null)
            return null;

        return UserStoryHistory.builder()
            .id(entity.getId())
            .status(entity.getStatus())
            .note(entity.getNote())
            // Audit Metadata
            .createdBy(entity.getCreatedBy())
            .createdDate(entity.getCreatedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static UserStoryHistory toDomain(
        UserStoryHistoryJpaEntity entity,
        BiConsumer<UserStoryHistory, UserStoryHistoryJpaEntity>... enrichers) {
        UserStoryHistory domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<UserStoryHistory, UserStoryHistoryJpaEntity>
                     enricher : enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachUserStory(UserStoryHistory domain,
                                       UserStoryHistoryJpaEntity entity) {
        if (entity.getUserStory() != null) {
            domain.setUserStory(
                UserStoryJpaMapper.toDomain(entity.getUserStory()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserStoryHistoryJpaEntity
    toJpaEntity(UserStoryHistory domain) {
        if (domain == null)
            return null;

        return UserStoryHistoryJpaEntity.builder()
            .id(domain.getId())
            .status(domain.getStatus())
            .note(domain.getNote())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static UserStoryHistoryJpaEntity toJpaEntity(
        UserStoryHistory domain,
        BiConsumer<UserStoryHistoryJpaEntity, UserStoryHistory>... enrichers) {
        UserStoryHistoryJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<UserStoryHistoryJpaEntity, UserStoryHistory>
                     enricher : enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachUserStory(UserStoryHistoryJpaEntity entity,
                                       UserStoryHistory domain) {
        if (domain.getUserStory() != null) {
            entity.setUserStory(
                UserStoryJpaMapper.toJpaEntity(domain.getUserStory()));
        }
    }
}
