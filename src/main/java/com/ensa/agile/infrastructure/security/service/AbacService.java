package com.ensa.agile.infrastructure.security.service;

import com.ensa.agile.application.global.service.IAbacService;
import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.domain.epic.repository.EpicRepository;
import com.ensa.agile.domain.product.entity.ProjectMember;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.product.repository.ProjectMemberRepository;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.sprint.repository.SprintMembersRepository;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.domain.task.repository.TaskRepository;
import com.ensa.agile.domain.user.entity.User;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ABAC authorization service for the project management domain.
 *
 * Purpose
 * -------
 * Centralizes all authorization decisions using:
 * - User attributes   (current authenticated user)
 * - Resource attributes (project, sprint, epic, story, task ownership)
 * - Role attributes   (PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER)
 *
 * No permission is granted without:
 * - Valid identifiers
 * - Correct resource-to-project ownership
 * - Required role
 * - Sprint membership where relevant
 *
 * Authorization Rules by Scope
 * -----------------------------
 *
 * PROJECT
 * - CREATE
 *   • Always allowed (project bootstrap).
 *
 * - INVITE_MEMBER
 *   • PRODUCT_OWNER
 *
 * - VIEW / VIEW_BACKLOG
 *   • PRODUCT_OWNER
 *   • SCRUM_MASTER
 *   • DEVELOPER
 *
 * - UPDATE
 *   • PRODUCT_OWNER
 *
 * - REMOVE_MEMBER
 *   • PRODUCT_OWNER
 *
 * EPIC
 * - CREATE
 *   • PRODUCT_OWNER
 *   • Valid project ID
 *
 * - VIEW
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *
 * - DELETE
 *   • Resource belongs to project
 *   • PRODUCT_OWNER
 *
 * USER STORY
 * - CREATE
 *   • PRODUCT_OWNER
 *
 * - VIEW
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *
 * - DELETE
 *   • Resource belongs to project
 *   • PRODUCT_OWNER
 *
 * - UPDATE_STATUS
 *   • Resource belongs to project
 *   • SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * SPRINT
 * - CREATE
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *
 * - VIEW
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * - MANAGE_STORIES
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *   • User must be sprint member
 *
 * - UPDATE_STATUS
 *   • SCRUM_MASTER
 *   • User must be sprint member
 *
 * - INVITE_MEMBER
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *   • User must be sprint member
 *
 * TASK
 * - CREATE
 *   • Valid project → sprint → story hierarchy
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * - VIEW
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * - UPDATE
 *   • PRODUCT_OWNER | SCRUM_MASTER
 *   • User must be sprint member
 *
 * - UPDATE_STATUS
 *   • SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * - ASSIGN
 *   • SCRUM_MASTER | DEVELOPER
 *   • User must be sprint member
 *
 * REPORTING
 * - VIEW_REPORT
 *   • Resource belongs to project
 *   • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * Enforcement Strategy
 * --------------------
 * - Ownership is verified via repository ID resolution
 * - Roles are checked at project scope only
 * - Sprint access always requires sprint membership
 * - Any exception or invalid state results in denial
 *
 * Security Model
 * --------------
 * Default-deny.
 * Fail-closed.
 * No implicit permissions.
 */
@RequiredArgsConstructor
@Component("abacService")
public class AbacService implements IAbacService {

    private final ICurrentUserService currentUserService;
    private final ProjectMemberRepository projectMemberRepository;
    private final SprintMembersRepository sprintMembersRepository;
    private final SprintBackLogRepository sprintBackLogRepository;
    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskRepository taskRepository;

    // --- Project Level ---
    @Override
    public boolean canCreateProject() {
        return true;
    }

    @Override
    public boolean canAccessProject(UUID projectId, String action) {
        return switch (action) {
            case "INVITE_MEMBER" ->
                hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "VIEW", "VIEW_BACKLOG" ->
                hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                               RoleType.SCRUM_MASTER, RoleType.DEVELOPER);
            case "REMOVE_MEMBER" ->
                hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "UPDATE" -> hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            default -> false;
        };
    }

    // --- Epic Level ---
    @Override
    public boolean canAccessEpic(UUID projectId, UUID epicId, String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);

            case "VIEW" ->
                isValideId(projectId, epicId) &&
                    validateOwnershipAndRole(
                        projectId, epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE" ->
                isValideId(projectId, epicId) &&
                    validateOwnershipAndRole(
                        projectId, epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);

            case "DELETE" ->
                isValideId(projectId, epicId) &&
                    validateOwnershipAndRole(
                        projectId, epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER);

            default -> false;
        };
    }

    // --- User Story Level ---
    @Override
    public boolean canAccessStory(UUID projectId, UUID sprintId, UUID storyId,
                                  String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "VIEW" ->
                isValideId(projectId, storyId) &&
                    validateOwnershipAndRole(
                        projectId, storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);
            case "UPDATE" ->
                isValideId(projectId, storyId) &&
                    validateOwnershipAndRole(
                        projectId, storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);
            case "DELETE" ->
                isValideId(projectId, storyId) &&
                    validateOwnershipAndRole(
                        projectId, storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER);
            case "UPDATE_STATUS" ->
                isValideId(projectId, sprintId, storyId) &&
                    validateOwnershipAndRole(
                        projectId, storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(sprintId);
            default -> false;
        };
    }

    // --- Sprint Level ---
    @Override
    public boolean canAccessSprint(UUID projectId, UUID sprintId,
                                   String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                                   RoleType.SCRUM_MASTER);

            case "VIEW" ->
                isValideId(projectId, sprintId) &&
                    validateOwnershipAndRole(
                        projectId, sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER) &&
                    isSprintMember(sprintId);

            case "MANAGE_STORIES" ->
                isValideId(projectId, sprintId) &&
                    validateOwnershipAndRole(
                        projectId, sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId);

            case "UPDATE_STATUS" ->
                isValideId(projectId, sprintId) &&
                    validateOwnershipAndRole(
                        projectId, sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId);

            case "INVITE_MEMBER" ->
                isValideId(projectId, sprintId) &&
                    validateOwnershipAndRole(
                        projectId, sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId);

            default -> false;
        };
    }

    // --- Task Level ---
    @Override
    public boolean canAccessTask(UUID projectId, UUID sprintId, UUID storyId,
                                 UUID taskId, String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId, sprintId, storyId) &&
                    canCreateTaskHelper(projectId, sprintId, storyId);

            case "VIEW" ->
                isValideId(projectId, taskId) &&
                    validateOwnershipAndRole(
                        projectId, taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE" ->
                isValideId(projectId, sprintId, taskId) &&
                    validateOwnershipAndRole(
                        projectId, taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);

            case "UPDATE_STATUS" ->
                isValideId(projectId, sprintId, taskId) &&
                    validateOwnershipAndRole(
                        projectId, taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER);

            case "ASSIGN" ->
                isValideId(projectId, sprintId, taskId) &&
                    validateOwnershipAndRole(
                        projectId, taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER);

            default -> false;
        } && isSprintMember(sprintId);
    }

    // --- Reporting ---
    @Override
    public boolean canViewReport(UUID projectId, UUID sprintId) {
        return validateOwnershipAndRole(
            projectId, sprintId,
            sprintBackLogRepository::getProductBackLogIdBySprintId,
            RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER, RoleType.DEVELOPER);
    }

    // --- Helper Methods ---
    private boolean canCreateTaskHelper(UUID projectId, UUID sprintId,
                                        UUID storyId) {
        boolean isValidHierarchy = false;
        try {
            UUID spId =
                userStoryRepository.getSprintBackLogIdByUserStoryId(storyId);
            UUID prId =
                sprintBackLogRepository.getProductBackLogIdBySprintId(sprintId);
            isValidHierarchy = sprintId.equals(spId) && projectId.equals(prId);
        } catch (Exception e) {
            return false;
        }

        return isValidHierarchy &&
            hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                           RoleType.SCRUM_MASTER, RoleType.DEVELOPER);
    }

    private boolean hasProjectRole(UUID projectId, RoleType... roles) {
        if (projectId == null)
            return false;
        try {
            User user = currentUserService.getCurrentUser();
            ProjectMember pm =
                projectMemberRepository.findByUserIdAndProductBackLogId(
                    user.getId(), projectId);
            return pm != null && Set.of(roles).contains(pm.getRole());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateOwnershipAndRole(UUID projectId, UUID resourceId,
                                             Function<UUID, UUID> idResolver,
                                             RoleType... roles) {
        try {
            UUID resolvedProjectId = idResolver.apply(resourceId);
            if (!projectId.equals(resolvedProjectId))
                return false;
            return hasProjectRole(projectId, roles);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSprintMember(UUID sprintId) {
        try {
            User currentUser = currentUserService.getCurrentUser();
            if (!sprintMembersRepository.existsBySprintBackLogIdAndUserId(
                    sprintId, currentUser.getId())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValideId(UUID... id) {
        for (UUID i : id) {
            if (i == null) {
                return false;
            }
        }
        return true;
    }
}
