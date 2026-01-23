package com.ensa.agile.infrastructure.persistence.jpa.sprint.history;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "sprint_histories",
       indexes =
       {
           @Index(name = "idx_sprint_history_sprint_backlog",
                  columnList = "sprint_backlog_id")
       })
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SprintHistoryJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SprintBackLogJpaEntity sprint;

    @Enumerated(EnumType.STRING) private SprintStatus status;

    private String note;
}
