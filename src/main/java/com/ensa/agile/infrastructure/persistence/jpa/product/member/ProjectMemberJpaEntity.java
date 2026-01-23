package com.ensa.agile.infrastructure.persistence.jpa.product.member;

import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@SuperBuilder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_members",
       uniqueConstraints =
       {
           @UniqueConstraint(name = "uk_user_project",
                             columnNames = {"user_id", "product_backlog_id"})
       },
       indexes =
       {
           @Index(name = "idx_project_member_product_backlog",
                  columnList = "product_backlog_id")
           ,
               @Index(name = "idx_project_member_user", columnList = "user_id")
       })
public class ProjectMemberJpaEntity extends BaseJpaEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserJpaEntity user;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_backlog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductBackLogJpaEntity productBackLog;

    @Enumerated(EnumType.STRING) private RoleType role;

    @Enumerated(EnumType.STRING) private MemberStatus status;
}
