package com.ensa.agile.infrastructure.persistence.jpa.story.history;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserStoryHistoryRepository
    extends JpaRepository<UserStoryHistoryJpaEntity, UUID> {
    List<UserStoryHistoryJpaEntity> findAllByUserStory_Id(UUID userStoryId);
}
