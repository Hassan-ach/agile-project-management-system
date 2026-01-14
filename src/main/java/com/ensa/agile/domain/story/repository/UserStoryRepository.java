package com.ensa.agile.domain.story.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.List;

public interface UserStoryRepository
    extends BaseDomainRepository<UserStory, String> {
    List<UserStory> findAllByEpicId(String epicId);
    List<UserStory> findByBatch(List<String> ids);
    void assignToSprint(List<String> userStoryIds, SprintBackLog sprintBackLog);
    String getSprintBackLogIdByUserStoryId(String userStoryId);
    String getProductBackLogIdByUserStoryId(String userStoryId);
}
