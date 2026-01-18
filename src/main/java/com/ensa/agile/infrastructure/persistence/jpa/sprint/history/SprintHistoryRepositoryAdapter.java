package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ensa.agile.application.sprint.exception.SprintHistoryNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class SprintHistoryRepositoryAdapter implements SprintHistoryRepository {
    private final JpaSprintHistoryRepository jpaSprintHistoryRepository;
    @Override
    public SprintHistory save(SprintHistory entity) {
        return SprintHistoryJpaMapper.toDomainEntity(
            jpaSprintHistoryRepository.save(
                SprintHistoryJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public SprintHistory findById(UUID s) {
        return jpaSprintHistoryRepository.findById(s)
            .map(SprintHistoryJpaMapper::toDomainEntity)
            .orElseThrow(SprintHistoryNotFoundException::new);
    }

    @Override
    public List<SprintHistory> findAll() {
        return jpaSprintHistoryRepository.findAll()
            .stream()
            .map(SprintHistoryJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        jpaSprintHistoryRepository.deleteById(s);
    }

    @Override
    public boolean existsById(UUID s) {
        return jpaSprintHistoryRepository.existsById(s);
    }

    @Override
    public List<SprintHistory> findAllBySprintId(UUID sprintId) {

        return jpaSprintHistoryRepository.findAllBySprint_Id(sprintId)
            .stream()
            .map(SprintHistoryJpaMapper::toDomainEntity)
            .toList();

    }
}
