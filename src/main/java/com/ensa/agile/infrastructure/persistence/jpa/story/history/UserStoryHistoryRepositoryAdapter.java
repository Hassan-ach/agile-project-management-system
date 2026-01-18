package com.ensa.agile.infrastructure.persistence.jpa.story.history;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ensa.agile.application.story.exception.UserStoryHistoryNotFoundException;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.repository.UserStoryHistoryRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserStoryHistoryRepositoryAdapter
    implements UserStoryHistoryRepository {

    private final JpaUserStoryHistoryRepository jpaUserStoryHistoryRepository;

    @Override
    public UserStoryHistory save(UserStoryHistory entity) {
        return UserStoryHistoryJpaMapper.toDomainEntity(
            jpaUserStoryHistoryRepository.save(
                UserStoryHistoryJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public UserStoryHistory findById(UUID s) {
        return jpaUserStoryHistoryRepository.findById(s)
            .map(UserStoryHistoryJpaMapper::toDomainEntity)
            .orElseThrow(UserStoryHistoryNotFoundException::new);
    }

    @Override
    public List<UserStoryHistory> findAll() {
        return jpaUserStoryHistoryRepository.findAll()
            .stream()
            .map(UserStoryHistoryJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        jpaUserStoryHistoryRepository.deleteById(s);
    }

    @Override
    public boolean existsById(UUID s) {
        return jpaUserStoryHistoryRepository.existsById(s);
    }

    @Override
    public List<UserStoryHistory> findAllByUserStoryId(UUID userStoryId) {
        return jpaUserStoryHistoryRepository.findAllByUserStory_Id(userStoryId)
            .stream()
            .map(UserStoryHistoryJpaMapper::toDomainEntity)
            .toList();
    }
}
