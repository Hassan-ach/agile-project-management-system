package com.ensa.agile.infrastructure.persistence.jpa.epic;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "epics",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_epic_title_product_backlog",
                             columnNames = {"title", "product_backlog_id"})
       },
       indexes =
       {
           @Index(name = "idx_epic_product_backlog",
                  columnList = "product_backlog_id")
       })
public class EpicJpaEntity extends BaseJpaEntity {

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductBackLogJpaEntity productBackLog;
}
