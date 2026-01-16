package com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.history.SprintHistoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JoinFormula;

@Table(name = "sprint_backlogs",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_sprint_name_product_backlog",
                             columnNames = {"name", "product_backlog_id"})
       },
       indexes =
       {
           @Index(name = "idx_sprint_backlog_product_backlog",
                  columnList = "product_backlog_id")
           ,
               @Index(name = "idx_sprint_backlog_scrum_master",
                      columnList = "scrum_master_id")
       })

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SprintBackLogJpaEntity extends BaseJpaEntity {

    @Column(nullable = false) private String name;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    private ProductBackLogJpaEntity productBackLog;

    @ManyToOne
    @JoinColumn(name = "scrum_master_id", nullable = false)
    private UserJpaEntity scrumMaster;

    @Column(name = "start_date", nullable = false) private LocalDate startDate;

    @Column(name = "end_date", nullable = false) private LocalDate endDate;

    @Column(nullable = false) private String goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("SELECT sh.id FROM sprint_backlogs_history sh WHERE "
                 + "sh.sprint_id = id ORDER BY sh.created_at DESC LIMIT 1 ")
    private SprintHistoryJpaEntity status;
}
