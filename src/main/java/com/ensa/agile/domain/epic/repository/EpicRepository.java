package com.ensa.agile.domain.epic.repository;

import com.ensa.agile.domain.epic.entity.Epic;
import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import java.util.List;

public interface EpicRepository extends BaseDomainRepository<Epic, String> {
    List<Epic> findAllByProductBackLogId(String productBackLogId);

    String getProductBackLogIdByEpicId(String epicId);
}
