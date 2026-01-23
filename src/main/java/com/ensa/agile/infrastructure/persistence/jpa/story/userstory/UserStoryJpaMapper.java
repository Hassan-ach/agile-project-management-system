package com.ensa.agile.infrastructure.persistence.jpa.story.userstory;

import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import com.ensa.agile.infrastructure.persistence.jpa.story.history.UserStoryHistoryJpaMapper;
import java.util.function.BiConsumer;

public class UserStoryJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserStory toDomain(UserStoryJpaEntity entity) {
        if (entity == null)
            return null;

        return UserStory.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .priority(entity.getPriority())
            .storyPoints(entity.getStoryPoints())
            .acceptanceCriteria(entity.getAcceptanceCriteria())
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
    public static UserStory
    toDomain(UserStoryJpaEntity entity,
             BiConsumer<UserStory, UserStoryJpaEntity>... enrichers) {
        UserStory domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<UserStory, UserStoryJpaEntity> enricher :
                 enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    public static void attachStatus(UserStory domain,
                                    UserStoryJpaEntity entity) {
        if (entity.getStatus() != null) {
            domain.setStatus(
                UserStoryHistoryJpaMapper.toDomain(entity.getStatus()));
        }
    }

    public static void attachProductBackLog(UserStory domain,
                                            UserStoryJpaEntity entity) {
        if (entity.getProductBackLog() != null) {
            domain.setProductBackLog(
                ProductBackLogJpaMapper.toDomain(entity.getProductBackLog()));
        }
    }

    public static void attachEpic(UserStory domain, UserStoryJpaEntity entity) {
        if (entity.getEpic() != null) {
            domain.setEpic(EpicJpaMapper.toDomain(entity.getEpic()));
        }
    }

    public static void attachSprintBackLog(UserStory domain,
                                           UserStoryJpaEntity entity) {
        if (entity.getSprintBackLog() != null) {
            domain.setSprintBackLog(
                SprintBackLogJpaMapper.toDomain(entity.getSprintBackLog()));
        }
    }

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserStoryJpaEntity toJpaEntity(UserStory domain) {
        if (domain == null)
            return null;

        return UserStoryJpaEntity.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
            .priority(domain.getPriority())
            .storyPoints(domain.getStoryPoints())
            .acceptanceCriteria(domain.getAcceptanceCriteria())
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
    public static UserStoryJpaEntity
    toJpaEntity(UserStory domain,
                BiConsumer<UserStoryJpaEntity, UserStory>... enrichers) {
        UserStoryJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<UserStoryJpaEntity, UserStory> enricher :
                 enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }

    // 3. Attachers
    public static void attachStatus(UserStoryJpaEntity entity,
                                    UserStory domain) {
        if (domain.getStatus() != null) {
            entity.setStatus(
                UserStoryHistoryJpaMapper.toJpaEntity(domain.getStatus()));
        }
    }

    public static void attachProductBackLog(UserStoryJpaEntity entity,
                                            UserStory domain) {
        if (domain.getProductBackLog() != null) {
            entity.setProductBackLog(ProductBackLogJpaMapper.toJpaEntity(
                domain.getProductBackLog()));
        }
    }

    public static void attachEpic(UserStoryJpaEntity entity, UserStory domain) {
        if (domain.getEpic() != null) {
            entity.setEpic(EpicJpaMapper.toJpaEntity(domain.getEpic()));
        }
    }

    public static void attachSprintBackLog(UserStoryJpaEntity entity,
                                           UserStory domain) {
        if (domain.getSprintBackLog() != null) {
            entity.setSprintBackLog(
                SprintBackLogJpaMapper.toJpaEntity(domain.getSprintBackLog()));
        }
    }
}
