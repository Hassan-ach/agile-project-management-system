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
        return TaskJpaMapper.toDomain(
            jpaTaskRepository.save(
                TaskJpaMapper.toJpaEntity(entity, TaskJpaMapper::attachAssignee,
                                          TaskJpaMapper::attachUserStory,
                                          TaskJpaMapper::attachSprintBackLog)),
            TaskJpaMapper::attachStatus, TaskJpaMapper::attachAssignee);
    }

    @Override
    public Task findById(UUID id) {
        return jpaTaskRepository.findById(id)
            .map(t
                 -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus,
                                           TaskJpaMapper::attachAssignee))
            .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll()
            .stream()
            .map(t -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus))
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

    @Override
    public List<Task> findAllByUserStoryId(UUID storyId) {
        return jpaTaskRepository.findAllByUserStory_Id(storyId)
            .stream()
            .map(t -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus))
            .toList();
    }

    @Override
    public UUID getSprintIdByTaskId(UUID taskId) {
        return jpaTaskRepository.geSprintIdByTaskId(taskId).orElseThrow(
            TaskNotFoundException::new);
    }

    @Override
    public List<Task> findAllByAssigneeId(UUID assigneeId) {
        return jpaTaskRepository.findAllByAssignee_Id(assigneeId)
            .stream()
            .map(t -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus))
            .toList();
    }

    @Override
    public List<Task> findAllBySprintId(UUID sprintId) {
        return jpaTaskRepository.findAllBySprintBackLog_Id(sprintId)
            .stream()
            .map(t -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus))
            .toList();
    }

    @Override
    public List<Task> findAllByProductBackLogId(UUID productId) {
        return jpaTaskRepository.findAllByUserStory_ProductBackLogId(productId)
            .stream()
            .map(t -> TaskJpaMapper.toDomain(t, TaskJpaMapper::attachStatus))
            .toList();
    }
}
