package com.ensa.agile.infrastructure.persistence.service;

import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.global.service.IFetchService;
import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.global.exception.BusinessRuleViolationException;
import com.ensa.agile.infrastructure.persistence.jpa.global.join.Row;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FetchService implements IFetchService {

    @PersistenceContext private EntityManager entityManager;

    // --- Template Constants to maintain Column Mapping Integrity ---

    private static final String PRODUCT_BACKLOG = """
        SELECT p.id AS productId, p.name AS productName, p.description AS productDescription,
        p.created_by AS productCreatedBy, p.created_date AS productCreatedDate,
        p.last_modified_by AS productLastModifiedBy, p.last_modified_date AS productLastModifiedDate
        """;
    private static final String MEMBER = """
            , pm.id AS projectMemberId, u.email AS projectMemberEmail, pm.role AS projectMemberRole, 
              pm.status AS projectMemberStatus, pm.created_by AS projectMemberInvitedBy, pm.created_date AS projectMemberInvitationDate
            """;
    private static final String SPRINT = """
            , s.id AS sprintId, s.name AS sprintName, u_s.email AS sprintScrumMasterEmail, s.start_date AS sprintStartDate, 
              s.end_date AS sprintEndDate, s.goal AS sprintGoal, s.created_by AS sprintCreatedBy, s.created_date AS sprintCreatedDate, 
              s.last_modified_by AS sprintLastModifiedBy, s.last_modified_date AS sprintLastModifiedDate,
              sh.id AS sprintHistoryId, sh.status AS sprintHistoryStatus, sh.note AS sprintHistoryNote
            """;
    private static final String SPRINT_MEMBER = """
            , sm.id AS sprintMemberId, u_sm.email AS sprintMemberEmail, sm.created_by AS sprintMemberInvitedBy, sm.created_date AS sprintMemberJoinedAt
            """;
    private static final String EPIC = """
            , e.id AS epicId, e.title AS epicTitle, e.description AS epicDescription, e.created_by AS epicCreatedBy, e.created_date AS epicCreatedDate, 
              e.last_modified_by AS epicLastModifiedBy, e.last_modified_date AS epicLastModifiedDate
            """;
    private static final String STORY = """
            , us.id AS storyId, us.title AS storyTitle, us.description AS storyDescription, us.priority AS storyPriority, 
              us.story_points AS storyPoints, us.acceptance_criteria AS storyAcceptanceCriteria, us.created_by AS storyCreatedBy, 
              us.created_date AS storyCreatedDate, us.last_modified_by AS storyLastModifiedBy, us.last_modified_date AS storyLastModifiedDate, 
              ush.id AS storyHistoryId, ush.status AS storyHistoryStatus, ush.note AS storyHistoryNote
            """;
    private static final String TASK = """
    , t.id AS taskId, t.title AS taskTitle, t.description AS taskDescription, t.assignee_id AS taskAssignee, 
      t.estimated_hours AS taskEstimatedHours, t.actual_hours AS taskActualHours, t.created_by AS taskCreatedBy, 
      t.created_date AS taskCreatedDate, t.last_modified_by AS taskLastModifiedBy, t.last_modified_date AS taskLastModifiedDate, 
      th.id AS taskHistoryId, th.status AS taskHistoryStatus, th.note AS taskHistoryNote
    """;
    private static final String NULL_PODUCT_BACKLOG =
        "SELECT CAST(NULL AS VARCHAR) AS productId, CAST(NULL AS VARCHAR) AS "
        + "productName, CAST(NULL AS VARCHAR) AS productDescription, "
        + "CAST(NULL AS VARCHAR) AS productCreatedBy, CAST(NULL AS "
        + "TIMESTAMP) AS productCreatedDate, CAST(NULL AS VARCHAR) AS "
        + "productLastModifiedBy, CAST(NULL AS TIMESTAMP) AS "
        + "productLastModifiedDate";
    private static final String NULL_MEMBER =
        ", CAST(NULL AS VARCHAR) AS projectMemberId, CAST(NULL AS VARCHAR) "
        + "AS projectMemberEmail, CAST(NULL AS VARCHAR) AS projectMemberRole, "
        + "CAST(NULL AS VARCHAR) AS projectMemberStatus, CAST(NULL AS VARCHAR) "
        + "AS projectMemberInvitedBy, CAST(NULL AS TIMESTAMP) AS "
        + "projectMemberInvitationDate";
    private static final String NULL_SPRINT =
        ", CAST(NULL AS VARCHAR) AS sprintId, CAST(NULL AS VARCHAR) AS "
        + "sprintName, CAST(NULL AS VARCHAR) AS sprintScrumMasterEmail, "
        + "CAST(NULL AS DATE) AS sprintStartDate, CAST(NULL AS DATE) AS "
        + "sprintEndDate, CAST(NULL AS VARCHAR) AS sprintGoal, CAST(NULL AS "
        + "VARCHAR) AS sprintCreatedBy, CAST(NULL AS TIMESTAMP) AS "
        + "sprintCreatedDate, CAST(NULL AS VARCHAR) AS sprintLastModifiedBy, "
        + "CAST(NULL AS TIMESTAMP) AS sprintLastModifiedDate, CAST(NULL AS "
        + "VARCHAR) AS sprintHistoryId, CAST(NULL AS VARCHAR) AS "
        + "sprintHistoryStatus, CAST(NULL AS VARCHAR) AS sprintHistoryNote";
    private static final String NULL_SPRINT_MEMBER =
        ", CAST(NULL AS VARCHAR) AS sprintMemberId, CAST(NULL AS VARCHAR) AS "
        + "sprintMemberEmail, CAST(NULL AS VARCHAR) AS sprintMemberInvitedBy, "
        + "CAST(NULL AS TIMESTAMP) AS sprintMemberJoinedAt";
    private static final String NULL_EPIC =
        ", CAST(NULL AS VARCHAR) AS epicId, CAST(NULL AS VARCHAR) AS "
        + "epicTitle, CAST(NULL AS VARCHAR) AS epicDescription, CAST(NULL AS "
        + "VARCHAR) AS epicCreatedBy, CAST(NULL AS TIMESTAMP) AS "
        + "epicCreatedDate, CAST(NULL AS VARCHAR) AS epicLastModifiedBy, "
        + "CAST(NULL AS TIMESTAMP) AS epicLastModifiedDate";
    private static final String NULL_STORY =
        ", CAST(NULL AS VARCHAR) AS storyId, CAST(NULL AS VARCHAR) AS "
        + "storyTitle, CAST(NULL AS VARCHAR) AS storyDescription, CAST(NULL AS "
        + "VARCHAR) AS storyPriority, CAST(NULL AS INTEGER) AS storyPoints, "
        + "CAST(NULL AS VARCHAR) AS storyAcceptanceCriteria, CAST(NULL AS "
        + "VARCHAR) AS storyCreatedBy, CAST(NULL AS TIMESTAMP) AS "
        + "storyCreatedDate, CAST(NULL AS VARCHAR) AS storyLastModifiedBy, "
        + "CAST(NULL AS TIMESTAMP) AS storyLastModifiedDate, CAST(NULL AS "
        + "VARCHAR) AS storyHistoryId, CAST(NULL AS VARCHAR) AS "
        + "storyHistoryStatus, CAST(NULL AS VARCHAR) AS storyHistoryNote";
    private static final String NULL_TASK =
        ", CAST(NULL AS VARCHAR) AS taskId, CAST(NULL AS VARCHAR) AS "
        + "taskTitle, CAST(NULL AS VARCHAR) AS taskDescription, CAST(NULL AS "
        + "VARCHAR) AS taskAssignee, CAST(NULL AS DOUBLE PRECISION) AS "
        + "taskEstimatedHours, CAST(NULL AS DOUBLE PRECISION) AS "
        + "taskActualHours, CAST(NULL AS VARCHAR) AS taskCreatedBy, CAST(NULL "
        + "AS TIMESTAMP) AS taskCreatedDate, CAST(NULL AS VARCHAR) AS "
        + "taskLastModifiedBy, CAST(NULL AS TIMESTAMP) AS "
        + "taskLastModifiedDate, CAST(NULL AS VARCHAR) AS taskHistoryId, "
        + "CAST(NULL AS VARCHAR) AS taskHistoryStatus, CAST(NULL AS VARCHAR) "
        + "AS taskHistoryNote";

    public List<Row> fetch(UUID id, Target target, List<String> fields) {
        FetchPlan plan = FetchPlan.resolve(fields);
        List<Row> results = new ArrayList<>();

        // 1. Always fetch Product Anchor
        results.addAll(execute(id, buildAnchorQuery(target)));

        // 2. Pass: Members
        if (plan.needsMembers(target)) {
            results.addAll(execute(id, buildMembersQuery()));
        }

        // 3. Pass: Sprints Branch (Includes Sprint Members, Stories, and Tasks
        // within Sprints)
        if (plan.needsSprints(target)) {
            results.addAll(execute(id, buildSprintDataQuery(plan, target)));
        }

        // 4. Pass: Epics Branch (Includes Stories and Tasks within Epics)
        if (plan.needsEpics(target)) {
            results.addAll(execute(id, buildEpicDataQuery(plan, target)));
        }

        // 5. Pass: Orphan Stories Branch
        if (plan.needsOrphanStories(target)) {
            results.addAll(execute(id, buildOrphanStoriesQuery(plan, target)));
        }

        return results;
    }

    private List<Row> execute(UUID id, String sql) {
        Query query =
            entityManager.createNativeQuery(sql, "ProductBackLogFetchMapping");
        query.setParameter("id", id);
        return query.getResultList();
    }

    private String buildAnchorQuery(Target target) {
        return switch (target) {
            case PRODUCT -> buildProductAnchorQuery();
            case SPRINT -> buildSprintAnschorQuery();
            case EPIC -> buildEpicAnchorQuery();
            case STORY -> buildStoryAnchorQuery();
        };
    }

    private String buildProductAnchorQuery() {
        return PRODUCT_BACKLOG + NULL_MEMBER + NULL_SPRINT +
            NULL_SPRINT_MEMBER + NULL_EPIC + NULL_STORY + NULL_TASK +
            " FROM product_backlogs p WHERE p.id = :id";
    }
    private String buildSprintAnschorQuery() {
        return NULL_PODUCT_BACKLOG + SPRINT + NULL_MEMBER + NULL_SPRINT_MEMBER +
            NULL_EPIC + NULL_STORY + NULL_TASK +
            " FROM sprint_backlogs s WHERE s.id = :id";
    }
    private String buildEpicAnchorQuery() {
        return NULL_PODUCT_BACKLOG + NULL_SPRINT + NULL_SPRINT_MEMBER + EPIC +
            NULL_MEMBER + NULL_STORY + NULL_TASK +
            " FROM epics e WHERE e.id = :id";
    }
    private String buildStoryAnchorQuery() {
        return NULL_PODUCT_BACKLOG + NULL_SPRINT + NULL_SPRINT_MEMBER +
            NULL_EPIC + STORY + NULL_MEMBER + NULL_TASK +
            " FROM user_stories us WHERE us.id = :id";
    }

    private String buildMembersQuery() {
        return PRODUCT_BACKLOG + MEMBER + NULL_SPRINT + NULL_SPRINT_MEMBER +
            NULL_EPIC + NULL_STORY + NULL_TASK + """
            FROM product_backlogs p
            JOIN project_members pm ON pm.product_backlog_id = p.id
            JOIN users u ON u.id = pm.user_id
            WHERE p.id = :id
            """;
    }

    private String buildSprintDataQuery(FetchPlan plan, Target target) {
        StringBuilder sql = switch (target) {
            case PRODUCT ->
                new StringBuilder(PRODUCT_BACKLOG + NULL_MEMBER + SPRINT);
            case SPRINT ->
                new StringBuilder(NULL_PODUCT_BACKLOG + NULL_MEMBER + SPRINT);
            case EPIC, STORY ->
                throw new BusinessRuleViolationException(
                    "Sprints data cannot be fetched when target is EPIC or "
                    + "STORY");
        };
        sql.append(plan.sprintMembers ? SPRINT_MEMBER : NULL_SPRINT_MEMBER);
        sql.append(NULL_EPIC);
        sql.append(plan.sprintStories ? STORY : NULL_STORY);
        sql.append(plan.sprintStoryTasks ? TASK : NULL_TASK);

        switch (target) {
        case PRODUCT ->
            sql.append(" FROM product_backlogs p JOIN sprint_backlogs s ON "
                       + "s.product_backlog_id = p.id ");
        case SPRINT -> sql.append(" FROM sprint_backlogs s ");
        case EPIC, STORY -> {
        }
        }

        sql.append(" LEFT JOIN users u_s ON u_s.id = s.scrum_master_id ");
        sql.append(" LEFT JOIN sprint_histories sh ON sh.id = (SELECT id "
                   + "FROM sprint_histories WHERE sprint_backlog_id = s.id "
                   + "ORDER BY created_date DESC LIMIT 1) ");

        if (plan.sprintMembers) {
            sql.append(" LEFT JOIN sprint_members sm ON sm.sprint_backlog_id "
                       +
                       "= s.id LEFT JOIN users u_sm ON u_sm.id = sm.user_id ");
        }
        if (plan.sprintStories) {
            sql.append(" LEFT JOIN user_stories us ON "
                       + "us.sprint_backlog_id = s.id ");
            sql.append(" LEFT JOIN user_story_histories ush ON ush.id = "
                       + "(SELECT id "
                       + "FROM user_story_histories WHERE user_story_id "
                       + "= us.id ORDER "
                       + "BY created_date DESC LIMIT 1) ");
            if (plan.sprintStoryTasks) {
                sql.append(" LEFT JOIN tasks t ON t.user_story_id = us.id ");
                sql.append(" LEFT JOIN task_histories th ON th.id = (SELECT "
                           + "id FROM task_histories WHERE task_id = t.id "
                           + "ORDER BY created_date DESC LIMIT 1) ");
            }
        }

        switch (target) {
        case PRODUCT -> sql.append(" WHERE p.id = :id");
        case SPRINT -> sql.append(" WHERE s.id = :id");
        case EPIC, STORY -> {
        }
        }
        return sql.toString();
    }

    private String buildEpicDataQuery(FetchPlan plan, Target target) {
        StringBuilder sql = switch (target) {
            case PRODUCT ->
                new StringBuilder(PRODUCT_BACKLOG + NULL_MEMBER + NULL_SPRINT +
                                  NULL_SPRINT_MEMBER + EPIC);
            case EPIC ->
                new StringBuilder(NULL_PODUCT_BACKLOG + NULL_MEMBER +
                                  NULL_SPRINT + NULL_SPRINT_MEMBER + EPIC);
            case STORY, SPRINT ->
                throw new BusinessRuleViolationException(
                    "Epic data cannot be fetched when target is STORY or "
                    + "SPRINT");
        };

        sql.append(plan.epicStories ? STORY : NULL_STORY);
        sql.append(plan.epicStoryTasks ? TASK : NULL_TASK);
        switch (target) {
        case PRODUCT ->
            sql.append(" FROM product_backlogs p JOIN epics e ON "
                       + "e.product_backlog_id = p.id ");
        case EPIC -> sql.append(" FROM epics e ");
        case STORY, SPRINT -> {
        }
        };

        if (plan.epicStories) {
            sql.append(" LEFT JOIN user_stories us ON us.epic_id = e.id ");
            sql.append(
                " LEFT JOIN user_story_histories ush ON ush.id = (SELECT id "
                + "FROM user_story_histories WHERE user_story_id = us.id ORDER "
                + "BY created_date DESC LIMIT 1) ");
            if (plan.epicStoryTasks) {
                sql.append(" LEFT JOIN tasks t ON t.user_story_id = us.id ");
                sql.append(" LEFT JOIN task_histories th ON th.id = (SELECT "
                           + "id FROM task_histories WHERE task_id = t.id "
                           + "ORDER BY created_date DESC LIMIT 1) ");
            }
        }

        switch (target) {
        case PRODUCT -> sql.append(" WHERE p.id = :id");
        case EPIC -> sql.append(" WHERE e.id = :id");
        case STORY, SPRINT -> {
        }
        };
        return sql.toString();
    }

    private String buildOrphanStoriesQuery(FetchPlan plan, Target target) {
        StringBuilder sql = switch (target) {
            case PRODUCT ->
                new StringBuilder(PRODUCT_BACKLOG + NULL_MEMBER + NULL_SPRINT +
                                  NULL_SPRINT_MEMBER + NULL_EPIC + STORY);
            case SPRINT ->
                new StringBuilder(NULL_PODUCT_BACKLOG + NULL_MEMBER + SPRINT +
                                  NULL_SPRINT_MEMBER + NULL_EPIC + STORY);
            case EPIC ->
                throw new BusinessRuleViolationException(
                    "Orphan Stories data cannot be fetched when target is "
                    + " EPIC");
            case STORY ->
                new StringBuilder(NULL_PODUCT_BACKLOG + NULL_MEMBER +
                                  NULL_SPRINT + NULL_SPRINT_MEMBER + NULL_EPIC +
                                  STORY);
        };
        sql.append(plan.orphanStoryTasks ? TASK : NULL_TASK);

        switch (target) {
        case PRODUCT ->
            sql.append(" FROM product_backlogs p JOIN user_stories us ON "
                       + "us.product_backlog_id = p.id AND us.epic_id IS "
                       + "NULL");
        case SPRINT ->
            sql.append(" FROM sprint_backlogs s JOIN user_stories us ON "
                       + "us.sprint_backlog_id = s.id AND us.epic_id IS "
                       + "NULL ");
        case STORY -> sql.append(" FROM user_stories us ");
        case EPIC -> {
        }
        }
        sql.append(" LEFT JOIN user_story_histories ush ON ush.id = (SELECT "
                   + "id FROM user_story_histories WHERE user_story_id = us.id "
                   + "ORDER BY created_date DESC LIMIT 1) ");
        if (plan.orphanStoryTasks) {
            sql.append(" LEFT JOIN tasks t ON t.user_story_id = us.id ");
            sql.append(" LEFT JOIN task_histories th ON th.id = (SELECT id "
                       + "FROM task_histories WHERE task_id = t.id ORDER BY "
                       + "created_date DESC LIMIT 1) ");
        }
        switch (target) {
        case PRODUCT -> sql.append(" WHERE p.id = :id");
        case SPRINT -> sql.append(" WHERE s.id = :id");
        case STORY -> sql.append(" WHERE us.id = :id");
        case EPIC -> {
        }
        }
        return sql.toString();
    }

    @Override
    public ProductBackLogResponse getResponse(ProductBackLogGetRequest req) {
        return Transformer.transform(
            this.fetch(req.getId(), Target.PRODUCT, req.getFields()));
    }

    @Override
    public SprintBackLogResponse getResponse(SprintBackLogGetRequest req) {
        List<Row> rows =
            this.fetch(req.getId(), Target.SPRINT, req.getFields());
        ProductBackLogResponse root = Transformer.transform(rows);
        return root.getSprintBackLogs().isEmpty()
            ? null
            : root.getSprintBackLogs().get(0);
    }

    @Override
    public EpicResponse getResponse(EpicGetRequest req) {
        List<Row> rows = this.fetch(req.getId(), Target.EPIC, req.getFields());
        ProductBackLogResponse root = Transformer.transform(rows);
        return root.getEpics().isEmpty() ? null : root.getEpics().get(0);
    }

    @Override
    public UserStoryResponse getResponse(UserStoryGetRequest req) {
        List<Row> rows = this.fetch(req.getId(), Target.STORY, req.getFields());
        ProductBackLogResponse root = Transformer.transform(rows);
        return root.getUserStories().isEmpty() ? null
                                               : root.getUserStories().get(0);
    }
}
