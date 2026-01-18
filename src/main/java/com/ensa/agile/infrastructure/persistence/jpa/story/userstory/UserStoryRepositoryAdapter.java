package com.ensa.agile.infrastructure.persistence.jpa.story.userstory;

import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserStoryRepositoryAdapter implements UserStoryRepository {

    private final JpaUserStoryRepository jpaUserStoryRepository;

    @Override
    public UserStory save(UserStory entity) {
        return UserStoryJpaMapper.toDomainEntity(
            this.jpaUserStoryRepository.save(
                UserStoryJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public UserStory findById(UUID s) {
        return UserStoryJpaMapper.toDomainEntity(
            this.jpaUserStoryRepository.findById(s).orElseThrow(
                UserStoryNotFoundException::new));
    }

    @Override
    public List<UserStory> findAll() {
        return this.jpaUserStoryRepository.findAll()
            .stream()
            .map(UserStoryJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        this.jpaUserStoryRepository.deleteById(s);
    }

    @Override
    public boolean existsById(UUID s) {
        return this.jpaUserStoryRepository.existsById(s);
    }

    @Override
    public List<UserStory> findAllByEpicId(UUID epicId) {
        return this.jpaUserStoryRepository.findAllByEpic_Id(epicId)
            .stream()
            .map(UserStoryJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public List<UserStory> findByBatch(List<UUID> ids) {
        return this.jpaUserStoryRepository.findByBatch(ids)
            .stream()
            .map(UserStoryJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void assignToSprint(List<UUID> userStoryIds,
                               SprintBackLog sprintBackLog) {
        this.jpaUserStoryRepository.assignToSprint(
            userStoryIds, SprintBackLogJpaMapper.toJpaEntity(sprintBackLog));
    }

    @Override
    public UUID getSprintBackLogIdByUserStoryId(UUID userStoryId) {
        return this.jpaUserStoryRepository
            .getSprintBackLogIdByUserStoryId(userStoryId)
            .orElseThrow(UserStoryNotFoundException::new);
    }

    @Override
    public UUID getProductBackLogIdByUserStoryId(UUID userStoryId) {
        return this.jpaUserStoryRepository
            .getProductBackLogIdByUserStoryId(userStoryId)
            .orElseThrow(UserStoryNotFoundException::new);
    }

    @Override
    public List<UserStory> findAllBySprintId(UUID sprintId) {
        return this.jpaUserStoryRepository.findAllBySprintBackLog_Id(sprintId)
            .stream()
            .map(UserStoryJpaMapper::toDomainEntityPartial)
            .toList();
    }
}
