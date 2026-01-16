package com.ensa.agile.infrastructure.persistence.jpa.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    private UUID createdBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", insertable = false)
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", insertable = false)
    private UUID lastModifiedBy;

    public BaseJpaEntity(UUID id) { this.id = id; }
}
