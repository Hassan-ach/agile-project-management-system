package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SprintBackLogRepositoryAdapter implements SprintBackLogRepository {
    private final JpaSprintBackLogRepository jpaSprintBackLogRepository;

    @Override
    public SprintBackLog save(SprintBackLog entity) {
        return SprintBackLogJpaMapper.toDomainEntity(
            this.jpaSprintBackLogRepository.save(
                SprintBackLogJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public SprintBackLog findById(UUID s) {
        return this.jpaSprintBackLogRepository.findById(s)
            .map(SprintBackLogJpaMapper::toDomainEntity)
            .orElseThrow(SprintBackLogNotFoundException::new);
    }

    @Override
    public List<SprintBackLog> findAll() {
        return this.jpaSprintBackLogRepository.findAll()
            .stream()
            .map(SprintBackLogJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        this.jpaSprintBackLogRepository.deleteById(s);
    }

    @Override
    public boolean existsById(UUID s) {
        return this.jpaSprintBackLogRepository.existsById(s);
    }

    @Override
    public UUID getProductBackLogIdBySprintId(UUID sprintId) {
        return this.jpaSprintBackLogRepository.getProductBackLogIdBySprintId(
            sprintId);
    }
}
