package com.ensa.agile.domain.global.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class BaseDomainEntity {
    private final UUID id;
    private final LocalDateTime createdDate;
    private final UUID createdBy;
    private final LocalDateTime lastModifiedDate;
    private final UUID lastModifiedBy;

    public BaseDomainEntity(UUID id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.createdBy = null;
        this.lastModifiedDate = null;
        this.lastModifiedBy = null;
    }
}
