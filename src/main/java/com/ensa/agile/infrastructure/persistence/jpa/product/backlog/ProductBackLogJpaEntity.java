package com.ensa.agile.infrastructure.persistence.jpa.product.backlog;

import com.ensa.agile.infrastructure.persistence.jpa.global.entity.BaseJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.global.join.Row;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SqlResultSetMapping(
    name = "ProductBackLogFetchMapping",
    classes = @ConstructorResult(
        targetClass = Row.class,
        columns =
        {
            // Product
            // (7)
            @ColumnResult(name = "productId", type = String.class)
            , @ColumnResult(name = "productName", type = String.class),
                @ColumnResult(name = "productDescription", type = String.class),
                @ColumnResult(name = "productCreatedBy", type = String.class),
                @ColumnResult(name = "productCreatedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "productLastModifiedBy",
                              type = String.class),
                @ColumnResult(name = "productLastModifiedDate",
                              type = LocalDateTime.class),

                // Project Member (6)
                @ColumnResult(name = "projectMemberId", type = String.class),
                @ColumnResult(name = "projectMemberEmail", type = String.class),
                @ColumnResult(name = "projectMemberRole", type = String.class),
                @ColumnResult(name = "projectMemberStatus",
                              type = String.class),
                @ColumnResult(name = "projectMemberInvitedBy",
                              type = String.class),
                @ColumnResult(name = "projectMemberInvitationDate",
                              type = LocalDateTime.class),

                // Sprint Backlog (14)
                @ColumnResult(name = "sprintId", type = String.class),
                @ColumnResult(name = "sprintName", type = String.class),
                @ColumnResult(name = "sprintScrumMasterEmail",
                              type = String.class),
                @ColumnResult(name = "sprintStartDate", type = LocalDate.class),
                @ColumnResult(name = "sprintEndDate", type = LocalDate.class),
                @ColumnResult(name = "sprintGoal", type = String.class),
                @ColumnResult(name = "sprintCreatedBy", type = String.class),
                @ColumnResult(name = "sprintCreatedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "sprintLastModifiedBy",
                              type = String.class),
                @ColumnResult(name = "sprintLastModifiedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "sprintHistoryId", type = String.class),
                @ColumnResult(name = "sprintHistoryStatus",
                              type = String.class),
                @ColumnResult(name = "sprintHistoryNote", type = String.class),

                // Sprint Member (4)
                @ColumnResult(name = "sprintMemberId", type = String.class),
                @ColumnResult(name = "sprintMemberEmail", type = String.class),
                @ColumnResult(name = "sprintMemberInvitedBy",
                              type = String.class),
                @ColumnResult(name = "sprintMemberJoinedAt",
                              type = LocalDateTime.class),

                // Epic (7)
                @ColumnResult(name = "epicId", type = String.class),
                @ColumnResult(name = "epicTitle", type = String.class),
                @ColumnResult(name = "epicDescription", type = String.class),
                @ColumnResult(name = "epicCreatedBy", type = String.class),
                @ColumnResult(name = "epicCreatedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "epicLastModifiedBy", type = String.class),
                @ColumnResult(name = "epicLastModifiedDate",
                              type = LocalDateTime.class),

                // User Story (13)
                @ColumnResult(name = "storyId", type = String.class),
                @ColumnResult(name = "storyTitle", type = String.class),
                @ColumnResult(name = "storyDescription", type = String.class),
                @ColumnResult(name = "storyPriority", type = String.class),
                @ColumnResult(name = "storyPoints", type = Integer.class),
                @ColumnResult(name = "storyAcceptanceCriteria",
                              type = String.class),
                @ColumnResult(name = "storyCreatedBy", type = String.class),
                @ColumnResult(name = "storyCreatedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "storyLastModifiedBy",
                              type = String.class),
                @ColumnResult(name = "storyLastModifiedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "storyHistoryId", type = String.class),
                @ColumnResult(name = "storyHistoryStatus", type = String.class),
                @ColumnResult(name = "storyHistoryNote", type = String.class),

                // Task (13)
                @ColumnResult(name = "taskId", type = String.class),
                @ColumnResult(name = "taskTitle", type = String.class),
                @ColumnResult(name = "taskDescription", type = String.class),
                @ColumnResult(name = "taskAssignee", type = String.class),
                @ColumnResult(name = "taskEstimatedHours", type = Double.class),
                @ColumnResult(name = "taskActualHours", type = Double.class),
                @ColumnResult(name = "taskCreatedBy", type = String.class),
                @ColumnResult(name = "taskCreatedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "taskLastModifiedBy", type = String.class),
                @ColumnResult(name = "taskLastModifiedDate",
                              type = LocalDateTime.class),
                @ColumnResult(name = "taskHistoryId", type = String.class),
                @ColumnResult(name = "taskHistoryStatus", type = String.class),
                @ColumnResult(name = "taskHistoryNote", type = String.class)
        }))
@Getter
@SuperBuilder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_backlogs")
public class ProductBackLogJpaEntity extends BaseJpaEntity {

    @Column(nullable = false) private String name;

    @Column(nullable = false) private String description;

    public ProductBackLogJpaEntity(UUID id, String name, String description,
                                   LocalDateTime createdDate, UUID createdBy,
                                   LocalDateTime lastModifiedDate,
                                   UUID lastModifiedBy) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy);
        this.name = name;
        this.description = description;
    }
}
