package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import com.ensa.agile.application.sprint.exception.SprintHistoryNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.repository.SprintHistoryRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class SprintHistoryRepositoryAdapter implements SprintHistoryRepository {
    private final JpaSprintHistoryRepository jpaSprintHistoryRepository;
    @Override
    public SprintHistory save(SprintHistory entity) {
        return SprintHistoryJpaMapper.toDomain(
            jpaSprintHistoryRepository.save(SprintHistoryJpaMapper.toJpaEntity(
                entity, SprintHistoryJpaMapper::attachSprint)));
    }

    @Override
    public SprintHistory findById(UUID s) {
        return jpaSprintHistoryRepository.findById(s)
            .map(SprintHistoryJpaMapper::toDomain)
            .orElseThrow(SprintHistoryNotFoundException::new);
    }

    @Override
    public List<SprintHistory> findAll() {
        return jpaSprintHistoryRepository.findAll()
            .stream()
            .map(SprintHistoryJpaMapper::toDomain)
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
            .map(SprintHistoryJpaMapper::toDomain)
            .toList();
    }
}
