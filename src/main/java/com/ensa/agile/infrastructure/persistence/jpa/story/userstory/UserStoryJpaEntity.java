package com.ensa.agile.infrastructure.persistence.jpa.story.userstory;

import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.infrastructure.persistence.jpa.epic.EpicJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.story.history.UserStoryHistoryJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "user_stories",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_user_story_title_product_backlog",
                             columnNames = {"title", "product_backlog_id"})
       },
       indexes =
       {
           @Index(name = "idx_user_story_product_backlog",
                  columnList = "product_backlog_id")
           ,
               @Index(name = "idx_user_story_sprint_backlog",
                      columnList = "sprint_backlog_id"),
               @Index(name = "idx_user_story_epic", columnList = "epic_id"),
       })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserStoryJpaEntity extends BaseJpaEntity {
    @Column(nullable = false) private String title;

    @Column(nullable = false) private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MoscowType priority;

    @Column(name = "story_points") private Integer storyPoints;

    @Column(name = "acceptance_criteria") private String acceptanceCriteria;

    @ManyToOne
    @JoinColumn(name = "epic_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private EpicJpaEntity epic;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductBackLogJpaEntity productBackLog;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private SprintBackLogJpaEntity sprintBackLog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(
        "(SELECT ush.id FROM user_story_histories ush WHERE "
        + "ush.user_story_id = id ORDER BY ush.created_date DESC LIMIT 1)")
    private UserStoryHistoryJpaEntity status;
}
