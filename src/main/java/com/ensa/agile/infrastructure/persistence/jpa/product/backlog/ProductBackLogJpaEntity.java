package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_backlogs")
public class ProductBackLogJpaEntity extends BaseJpaEntity {

    @Column(nullable = false) private String name;

    @Column(nullable = false) private String description;
}
