package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import java.util.List;
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
    public SprintBackLog findById(String s) {
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
    public void deleteById(String s) {
        this.jpaSprintBackLogRepository.deleteById(s);
    }

    @Override
    public boolean existsById(String s) {
        return this.jpaSprintBackLogRepository.existsById(s);
    }

    @Override
    public boolean existsByStatus(SprintStatus status) {
        return this.jpaSprintBackLogRepository.existsByStatus(status);
    }

    @Override
    public String getProductBackLogIdBySprintId(String sprintId) {
        return this.jpaSprintBackLogRepository.getProductBackLogIdBySprintId(
            sprintId);
    }
}
