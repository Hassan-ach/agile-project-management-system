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
 * - Sprint membership where relevant (for modifications)
 *
 * Authorization Rules by Scope
 * -----------------------------
 *
 * PROJECT
 * - CREATE
 * • Always allowed (project bootstrap).
 *
 * - INVITE_MEMBER / REMOVE_MEMBER
 * • PRODUCT_OWNER
 *
 * - VIEW / VIEW_BACKLOG
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE / DELETE
 * • PRODUCT_OWNER
 *
 * EPIC
 * - CREATE / DELETE
 * • PRODUCT_OWNER
 *
 * - VIEW
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE
 * • PRODUCT_OWNER | SCRUM_MASTER
 *
 * USER STORY
 * - CREATE / DELETE
 * • PRODUCT_OWNER
 *
 * - VIEW
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE
 * • PRODUCT_OWNER | SCRUM_MASTER
 *
 * - UPDATE_STATUS
 * • SCRUM_MASTER | DEVELOPER
 * • User must be sprint member
 *
 * SPRINT
 * - CREATE
 * • PRODUCT_OWNER | SCRUM_MASTER
 *
 * - VIEW
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - MANAGE_STORIES / INVITE_MEMBER
 * • PRODUCT_OWNER | SCRUM_MASTER
 * • User must be sprint member
 *
 * - UPDATE_STATUS
 * • SCRUM_MASTER
 * • User must be sprint member
 *
 * - DELETE
* • PRODUCT_OWNER | SCRUM_MASTER (must be sprint member)
 *
 * TASK
 * - CREATE
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 * • User must be sprint member (via User Story linkage)
 *
 * - VIEW
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 *
 * - UPDATE (Modify Details)
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 * • User must be sprint member
 *
 * - UPDATE_STATUS / ASSIGN / UNASSIGN
 * • SCRUM_MASTER | DEVELOPER
 * • User must be sprint member
 *
 * - DELETE
 * • PRODUCT_OWNER | SCRUM_MASTER
 * • User must be sprint member
 *
 * REPORTING
 * - SPRINT
 * • PRODUCT_OWNER | SCRUM_MASTER | DEVELOPER
 * - PROJECT
 * • PRODUCT_OWNER
 *
 * Security Model
 * --------------
 * Default-deny. Fail-closed.
 * Ownership is verified via repository ID resolution.
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
            case "DELETE" -> hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
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
                isValideId(epicId) &&
                    validateRole(epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE" ->
                isValideId(epicId) &&
                    validateRole(epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);

            case "DELETE" ->
                isValideId(epicId) &&
                    validateRole(epicId,
                        epicRepository::getProductBackLogIdByEpicId,
                        RoleType.PRODUCT_OWNER);

            default -> false;
        };
    }

    // --- User Story Level ---
    @Override
    public boolean canAccessStory(UUID projectId, UUID storyId,
                                  String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "VIEW" ->
                isValideId(storyId) &&
                    validateRole(storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);
            case "UPDATE" ->
                isValideId(storyId) &&
                    validateRole(storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);
            case "DELETE" ->
                isValideId(storyId) &&
                    validateRole(storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER);
            case "UPDATE_STATUS" ->
                isValideId(storyId) &&
                    validateRole(storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(storyId,
                        userStoryRepository::getSprintBackLogIdByUserStoryId);
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
                isValideId(sprintId) &&
                    validateRole(sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "MANAGE_STORIES" ->
                isValideId(sprintId) &&
                    validateRole(sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId, (UUID id)->{return id;});

            case "UPDATE_STATUS" ->
                isValideId(sprintId) &&
                    validateRole(sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId, (UUID id)->{return id;});

            case "INVITE_MEMBER" ->
                isValideId(sprintId) &&
                    validateRole(sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId, (UUID id)->{return id;});

            case "DELETE" ->
                isValideId(sprintId) &&(
                    validateRole(sprintId,
                sprintBackLogRepository::getProductBackLogIdBySprintId,
                RoleType.PRODUCT_OWNER) || 
                    (validateRole(sprintId,
                sprintBackLogRepository::getProductBackLogIdBySprintId, 
                RoleType.SCRUM_MASTER) && isSprintMember(sprintId, 
                (UUID id)->{return id;}))
                );

            default -> false;
        };
    }

    // --- Task Level ---
    @Override
    public boolean canAccessTask(UUID storyId,
                                 UUID taskId, String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(storyId) &&
            validateRole(storyId, 
                userStoryRepository::getProductBackLogIdByUserStoryId, 
                RoleType.PRODUCT_OWNER,
                RoleType.SCRUM_MASTER, RoleType.DEVELOPER) && isSprintMember(
                    storyId,
                    userStoryRepository::getSprintBackLogIdByUserStoryId)
            && isSprintMember(storyId, 
                userStoryRepository::getSprintBackLogIdByUserStoryId);

            case "VIEW" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER)
            && isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "UPDATE_STATUS" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER)
            && isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "ASSIGN" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                        taskRepository::getProductBackLogIdByTaskId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER)
            && isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "UNASSIGN" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                taskRepository::getProductBackLogIdByTaskId,
                RoleType.SCRUM_MASTER, RoleType.DEVELOPER)
            && isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "DELETE" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                taskRepository::getProductBackLogIdByTaskId,
                RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER)
            && isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            default -> false;
        } ;
    }

    // --- Reporting ---
    @Override
    public boolean canViewReport(UUID projectId, UUID sprintId, String action) {
        return switch (action) {
            case "SPRINT" ->
            isValideId(sprintId) &&
                validateRole(sprintId,
                    sprintBackLogRepository::getProductBackLogIdBySprintId,
                    RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                    RoleType.DEVELOPER);
            case "PROJECT" -> isValideId(projectId) &&
                hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            default -> false;
        };
    }

    // --- Helper Methods ---
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

    private boolean validateRole(UUID resourceId,
                                             Function<UUID, UUID> idResolver,
                                             RoleType... roles) {
        try {
            UUID projectId = idResolver.apply(resourceId);
            return hasProjectRole(projectId, roles);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSprintMember(UUID resourceId, Function<UUID,UUID> idResolver) {
        try {
            User currentUser = currentUserService.getCurrentUser();
            UUID sprintId = idResolver.apply(resourceId);
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
