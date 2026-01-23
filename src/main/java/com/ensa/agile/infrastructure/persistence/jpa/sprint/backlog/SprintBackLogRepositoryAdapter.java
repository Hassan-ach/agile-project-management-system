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
        return SprintBackLogJpaMapper.toDomain(
            this.jpaSprintBackLogRepository.save(
                SprintBackLogJpaMapper.toJpaEntity(
                    entity, SprintBackLogJpaMapper::attachScrumMaster,
                    SprintBackLogJpaMapper::attachProductBackLog)),
            SprintBackLogJpaMapper::attachStatus);
    }

    @Override
    public SprintBackLog findById(UUID s) {
        return this.jpaSprintBackLogRepository.findById(s)
            .map(sp
                 -> SprintBackLogJpaMapper.toDomain(
                     sp, SprintBackLogJpaMapper::attachStatus))
            .orElseThrow(SprintBackLogNotFoundException::new);
    }

    @Override
    public List<SprintBackLog> findAll() {
        return this.jpaSprintBackLogRepository.findAll()
            .stream()
            .map(s
                 -> SprintBackLogJpaMapper.toDomain(
                     s, SprintBackLogJpaMapper::attachStatus))
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

    @Override
    public List<SprintBackLog> findAllByProductBackLogId(UUID productId) {
        return this.jpaSprintBackLogRepository
            .findAllByProductBackLog_Id(productId)
            .stream()
            .map(s
                 -> SprintBackLogJpaMapper.toDomain(
                     s, SprintBackLogJpaMapper::attachStatus))
            .toList();
    }
}
