package com.ensa.agile.infrastructure.persistence.jpa.sprint.member;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.sprint.backlog.SprintBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "sprint_members",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_user_sprint",
                             columnNames = {"user_id", "sprint_backlog_id"})
       },
       indexes =
       {
           @Index(name = "idx_sprint_member_sprint_backlog",
                  columnList = "sprint_backlog_id")
           ,
               @Index(name = "idx_sprint_member_user", columnList = "user_id")
       })

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SprintMemberJpaEntity extends BaseJpaEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SprintBackLogJpaEntity sprintBackLog;
}
