package com.ensa.agile.infrastructure.persistence.jpa.task.task;

import com.ensa.agile.application.task.exception.TaskNotFoundException;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.repository.TaskRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private JpaTaskRepository jpaTaskRepository;

    @Override
    public Task save(Task entity) {
        return TaskJpaMapper.toDomainEntity(
            jpaTaskRepository.save(TaskJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public Task findById(UUID id) {
        return jpaTaskRepository.findById(id)
            .map(TaskJpaMapper::toDomainEntity)
            .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll()
            .stream()
            .map(TaskJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaTaskRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaTaskRepository.existsById(id);
    }

    @Override
    public UUID getProductBackLogIdByTaskId(UUID taskId) {
        return jpaTaskRepository.getProductBackLogIdByTaskId(taskId)
            .orElseThrow(TaskNotFoundException::new);
    }
}
