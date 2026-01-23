package com.ensa.agile.domain.story.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.List;
import java.util.UUID;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface UserStoryRepository
    extends BaseDomainRepository<UserStory, UUID> {
    List<UserStory> findAllByEpicId(UUID epicId);
    List<UserStory> findByBatch(List<UUID> ids);
    void assignToSprint(List<UUID> userStoryIds, SprintBackLog sprintBackLog);
    UUID getSprintBackLogIdByUserStoryId(UUID userStoryId);
    UUID getProductBackLogIdByUserStoryId(UUID userStoryId);

    List<UserStory> findAllBySprintId(UUID sprintId);
    List<UserStory> findAllByProductBackLogId(UUID productId);
}
