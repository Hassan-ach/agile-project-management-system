package com.ensa.agile.domain.product.repository;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.product.entity.ProductBackLog;
import java.util.UUID;

public interface ProductBackLogRepository
    extends BaseDomainRepository<ProductBackLog, UUID> {}
