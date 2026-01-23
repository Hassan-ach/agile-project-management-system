package com.ensa.agile.infrastructure.persistence.service;

import com.ensa.agile.infrastructure.persistence.jpa.product.backlog.ProductBackLogJpaEntity;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.EpicView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.MemberView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.ProjectView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.SprintMemberView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.SprintView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.StoryView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.TaskView;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BackLogRepo
    extends JpaRepository<ProductBackLogJpaEntity, UUID> {

    // =========================================================================
    //  SECTION 1: PRODUCT FETCH
    // =========================================================================
    @Query(value = """
        SELECT id, name, description,
               created_by AS createdBy,
               created_date AS createdDate
        FROM product_backlogs
        WHERE id = :id
        """, nativeQuery = true)
    ProjectView findProductHeader(@Param("id") UUID id);

    // 1. Fetch Sprints with Scrum Master Email & Latest History Status
    @Query(value = """
        SELECT s.id, s.name, s.goal, s.start_date AS startDate, s.end_date AS endDate,
               s.created_by AS createdBy, s.created_date AS createdDate,
               u.email AS scrumMasterEmail,
               h.status AS latestStatus
        FROM sprint_backlogs s
        LEFT JOIN users u ON u.id = s.scrum_master_id
        LEFT JOIN LATERAL (
            SELECT status FROM sprint_histories 
            WHERE sprint_backlog_id = s.id 
            ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE s.product_backlog_id = :pId
        """, nativeQuery = true)
    List<SprintView> findSprintsByProduct(@Param("pId") UUID productId);

    // 2. Fetch Epics
    @Query(value = """
        SELECT e.id, e.title, e.description, e.created_by AS createdBy, 
               e.created_date AS createdDate
        FROM epics e
        WHERE e.product_backlog_id = :pId
        """, nativeQuery = true)
    List<EpicView> findEpicsByProduct(@Param("pId") UUID productId);

    // 3. Fetch Members
    @Query(value = """
        SELECT pm.id, u.email, pm.role, pm.status, 
               pm.created_by AS invitedBy, pm.created_date AS invitationDate
        FROM project_members pm
        JOIN users u ON u.id = pm.user_id
        WHERE pm.product_backlog_id = :pId
        """, nativeQuery = true)
    List<MemberView> findMembersByProduct(@Param("pId") UUID productId);

    // 4. Fetch User Stories (With FKs for Epics/Sprints)
    @Query(value = """
        SELECT us.id, us.title, us.description, us.priority, us.story_points AS storyPoints,
               us.acceptance_criteria AS acceptanceCriteria,
               us.created_by AS createdBy, us.created_date AS createdDate,
               us.sprint_backlog_id AS sprintId,
               us.epic_id AS epicId,
               h.status AS latestStatus
        FROM user_stories us
        LEFT JOIN LATERAL (
            SELECT status FROM user_story_histories 
            WHERE user_story_id = us.id 
            ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.product_backlog_id = :pId
        """, nativeQuery = true)
    List<StoryView> findStoriesByProduct(@Param("pId") UUID productId);

    // 5. Fetch Tasks (With FK for Story)
    @Query(value = """
        SELECT t.id, t.title, t.description, t.estimated_hours AS estimatedHours,
               t.actual_hours AS actualHours, t.created_by AS createdBy,
               t.created_date AS createdDate,
               t.user_story_id AS userStoryId,
               t.assignee_id AS assigneeId,
               u.email AS assigneeEmail,
               h.status AS latestStatus
        FROM tasks t
        JOIN user_stories us ON us.id = t.user_story_id
        LEFT JOIN users u ON u.id = t.assignee_id
        LEFT JOIN LATERAL (
            SELECT status FROM task_histories 
            WHERE task_id = t.id 
            ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.product_backlog_id = :pId
        """, nativeQuery = true)
    List<TaskView> findTasksByProduct(@Param("pId") UUID productId);

    // =========================================================================
    //  SECTION 2: SPRINT FETCH
    // =========================================================================

    @Query(value = """
        SELECT s.id, s.name, s.goal, s.start_date AS startDate, s.end_date AS endDate,
               s.created_by AS createdBy, s.created_date AS createdDate,
               u.email AS scrumMasterEmail,
               h.status AS latestStatus
        FROM sprint_backlogs s
        LEFT JOIN users u ON u.id = s.scrum_master_id
        LEFT JOIN LATERAL (
            SELECT status FROM sprint_histories 
            WHERE sprint_backlog_id = s.id 
            ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE s.id = :id
        """, nativeQuery = true)
    SprintView findSprintHeader(@Param("id") UUID id);

    @Query(value = """
        SELECT sm.id, u.email AS userEmail, sm.created_by AS invitedBy, 
               sm.created_date AS joinedAt
        FROM sprint_members sm
        JOIN users u ON u.id = sm.user_id
        WHERE sm.sprint_backlog_id = :sprintId
        """, nativeQuery = true)
    List<SprintMemberView>
    findMembersBySprint(@Param("sprintId") UUID sprintId);

    // Reuse StoryView but filter by Sprint
    @Query(value = """
        SELECT us.id, us.title, us.description, us.priority, us.story_points AS storyPoints,
               us.acceptance_criteria AS acceptanceCriteria,
               us.created_by AS createdBy, us.created_date AS createdDate,
               us.sprint_backlog_id AS sprintId, us.epic_id AS epicId,
               h.status AS latestStatus
        FROM user_stories us
        LEFT JOIN LATERAL (
            SELECT status FROM user_story_histories 
            WHERE user_story_id = us.id 
            ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.sprint_backlog_id = :sprintId
        """, nativeQuery = true)
    List<StoryView> findStoriesBySprint(@Param("sprintId") UUID sprintId);

    // Filter Tasks by Sprint (Direct Link in Schema)
    @Query(value = """
        SELECT t.id, t.title, t.description, t.estimated_hours AS estimatedHours,
               t.actual_hours AS actualHours, t.created_by AS createdBy,
               t.created_date AS createdDate,
               t.user_story_id AS userStoryId, t.assignee_id AS assigneeId,
               u.email AS assigneeEmail,
               h.status AS latestStatus
        FROM tasks t
        LEFT JOIN users u ON u.id = t.assignee_id
        LEFT JOIN LATERAL (
            SELECT status FROM task_histories WHERE task_id = t.id ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE t.sprint_backlog_id = :sprintId
        """, nativeQuery = true)
    List<TaskView> findTasksBySprint(@Param("sprintId") UUID sprintId);

    // =========================================================================
    //  SECTION 3: EPIC FETCH
    // =========================================================================

    @Query(value = """
        SELECT e.id, e.title, e.description, e.created_by AS createdBy, 
               e.created_date AS createdDate
        FROM epics e
        WHERE e.id = :id
        """, nativeQuery = true)
    EpicView findEpicHeader(@Param("id") UUID id);

    // Filter Stories by Epic
    @Query(value = """
        SELECT us.id, us.title, us.description, us.priority, us.story_points AS storyPoints,
               us.acceptance_criteria AS acceptanceCriteria,
               us.created_by AS createdBy, us.created_date AS createdDate,
               us.sprint_backlog_id AS sprintId, us.epic_id AS epicId,
               h.status AS latestStatus
        FROM user_stories us
        LEFT JOIN LATERAL (
            SELECT status FROM user_story_histories WHERE user_story_id = us.id ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.epic_id = :epicId
        """, nativeQuery = true)
    List<StoryView> findStoriesByEpic(@Param("epicId") UUID epicId);

    // Filter Tasks by Epic (Requires Join)
    @Query(value = """
        SELECT t.id, t.title, t.description, t.estimated_hours AS estimatedHours,
               t.actual_hours AS actualHours, t.created_by AS createdBy,
               t.created_date AS createdDate,
               t.user_story_id AS userStoryId, t.assignee_id AS assigneeId,
               u.email AS assigneeEmail,
               h.status AS latestStatus
        FROM tasks t
        JOIN user_stories us ON us.id = t.user_story_id
        LEFT JOIN users u ON u.id = t.assignee_id
        LEFT JOIN LATERAL (
            SELECT status FROM task_histories WHERE task_id = t.id ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.epic_id = :epicId
        """, nativeQuery = true)
    List<TaskView> findTasksByEpic(@Param("epicId") UUID epicId);

    // =========================================================================
    //  SECTION 4: STORY FETCH
    // =========================================================================

    @Query(value = """
        SELECT us.id, us.title, us.description, us.priority, us.story_points AS storyPoints,
               us.acceptance_criteria AS acceptanceCriteria,
               us.created_by AS createdBy, us.created_date AS createdDate,
               us.sprint_backlog_id AS sprintId, us.epic_id AS epicId,
               h.status AS latestStatus
        FROM user_stories us
        LEFT JOIN LATERAL (
            SELECT status FROM user_story_histories WHERE user_story_id = us.id ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE us.id = :id
        """, nativeQuery = true)
    StoryView findStoryHeader(@Param("id") UUID id);

    // Filter Tasks by Story
    @Query(value = """
        SELECT t.id, t.title, t.description, t.estimated_hours AS estimatedHours,
               t.actual_hours AS actualHours, t.created_by AS createdBy,
               t.created_date AS createdDate,
               t.user_story_id AS userStoryId, t.assignee_id AS assigneeId,
               u.email AS assigneeEmail,
               h.status AS latestStatus
        FROM tasks t
        LEFT JOIN users u ON u.id = t.assignee_id
        LEFT JOIN LATERAL (
            SELECT status FROM task_histories WHERE task_id = t.id ORDER BY created_date DESC LIMIT 1
        ) h ON true
        WHERE t.user_story_id = :storyId
        """, nativeQuery = true)
    List<TaskView> findTasksByStory(@Param("storyId") UUID storyId);
}
