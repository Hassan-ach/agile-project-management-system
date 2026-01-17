package com.ensa.agile.infrastructure.persistence.jpa.task.task;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.userstory.UserStoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.task.history.TaskHistoryJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.JoinFormula;

@Table(name = "tasks",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_tiltle_user_story",
                             columnNames = {"title", "user_story_id"})
       },
       indexes =
       {
           @Index(name = "idx_task_user_story", columnList = "user_story_id")
           ,
               @Index(name = "idx_task_sprint_backlog",
                      columnList = "sprint_backlog_id"),
               @Index(name = "idx_task_assignee", columnList = "assignee_id")
       })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskJpaEntity extends BaseJpaEntity {
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String description;

    @ManyToOne
    @JoinColumn(name = "user_story_id", nullable = false)
    private UserStoryJpaEntity userStory;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id", nullable = false)
    private SprintBackLogJpaEntity sprintBackLog;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = true)
    private UserJpaEntity assignee;

    @Column(name = "estimated_hours", nullable = false)
    private Double estimatedHours;

    @Column(name = "actual_hours", nullable = true) private Double actualHours;

    @ManyToOne
    @JoinFormula("(SELECT th.id FROM task_histories th WHERE "
                 + "th.task_id = id ORDER BY th.created_date DESC LIMIT 1)")
    private TaskHistoryJpaEntity status;
}
