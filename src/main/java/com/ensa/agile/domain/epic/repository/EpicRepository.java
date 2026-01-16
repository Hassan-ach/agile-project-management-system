package com.ensa.agile.domain.epic.repository;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import java.util.List;
import java.util.UUID;

public interface EpicRepository extends BaseDomainRepository<Epic, UUID> {
    List<Epic> findAllByProductBackLogId(UUID productBackLogId);

    UUID getProductBackLogIdByEpicId(UUID epicId);
}
