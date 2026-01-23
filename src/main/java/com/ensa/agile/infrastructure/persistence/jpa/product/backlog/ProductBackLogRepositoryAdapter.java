package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.domain.global.exception.DataBasePersistenceException;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import com.ensa.agile.domain.product.repository.ProductBackLogRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductBackLogRepositoryAdapter
    implements ProductBackLogRepository {
    private final JpaProductBackLogRepository jpaProductBackLogRepository;

    @Override
    public ProductBackLog save(ProductBackLog entity) {
        try {
            var jpaEntity = ProductBackLogJpaMapper.toJpaEntity(entity);
            var saved = jpaProductBackLogRepository.save(jpaEntity);
            return ProductBackLogJpaMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DataBasePersistenceException();
        }
    }

    @Override
    public ProductBackLog findById(UUID s) {
        return this.jpaProductBackLogRepository.findById(s)
            .map(ProductBackLogJpaMapper::toDomain)
            .orElseThrow(ProductBackLogNotFoundException::new);
    }

    @Override
    public List<ProductBackLog> findAll() {
        return this.jpaProductBackLogRepository.findAll()
            .stream()
            .map(ProductBackLogJpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(UUID s) {
        try {
            this.jpaProductBackLogRepository.deleteById(s);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductBackLogNotFoundException();
        }
    }

    @Override
    public boolean existsById(UUID s) {
        return this.jpaProductBackLogRepository.existsById(s);
    }
}
