package com.ensa.agile.infrastructure.persistence.jpa.task.history;

import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.task.task.TaskJpaEntity;
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

@Table(name = "task_histories",
       indexes =
       { @Index(name = "idx_task_history_task", columnList = "task_id") })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskHistoryJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskJpaEntity task;

    @Enumerated(EnumType.STRING) private TaskStatus status;

    private String note;
}
