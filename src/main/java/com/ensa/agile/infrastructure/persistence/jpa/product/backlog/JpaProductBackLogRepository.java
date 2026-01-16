package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductBackLogRepository
    extends JpaRepository<ProductBackLogJpaEntity, UUID> {}
