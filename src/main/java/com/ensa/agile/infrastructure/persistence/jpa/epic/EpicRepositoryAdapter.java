package com.ensa.agile.infrastructure.persistence.jpa.epic;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EpicRepositoryAdapter implements EpicRepository {

    private final JpaEpicRepository jpaEpicRepository;

    @Override
    public Epic save(Epic entity) {
        return EpicJpaMapper.toDomainEntity(
            jpaEpicRepository.save(EpicJpaMapper.toJpaEntity(entity)));
    }

    @Override
    public Epic findById(String s) {
        return EpicJpaMapper.toDomainEntity(
            jpaEpicRepository.findById(s).orElseThrow(
                EpicNotFoundException::new));
    }

    @Override
    public List<Epic> findAll() {
        return jpaEpicRepository.findAll()
            .stream()
            .map(EpicJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(String s) {
        jpaEpicRepository.deleteById(s);
    }

    @Override
    public boolean existsById(String s) {
        return jpaEpicRepository.existsById(s);
    }

    @Override
    public List<Epic> findAllByProductBackLogId(String projectId) {
        return jpaEpicRepository.findAllByProductBackLog_Id(projectId)
            .stream()
            .map(EpicJpaMapper::toDomainEntity)
            .toList();
    }

    @Override
    public String getProductBackLogIdByEpicId(String epicId) {
        return this.jpaEpicRepository.getProductBackLogIdByEpicId(epicId)
            .orElseThrow(EpicNotFoundException::new);
    }
}
