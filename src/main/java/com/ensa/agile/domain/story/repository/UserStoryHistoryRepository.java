package com.ensa.agile.domain.story.repository;

import java.util.List;
import java.util.UUID;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.story.entity.UserStoryHistory;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface UserStoryHistoryRepository extends BaseDomainRepository<UserStoryHistory, UUID> {
    List<UserStoryHistory> findAllByUserStoryId(UUID storyId);

}
