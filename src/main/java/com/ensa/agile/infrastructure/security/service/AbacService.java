package com.ensa.agile.infrastructure.security.service;

import com.ensa.agile.application.global.service.IAbacService;
import com.ensa.agile.application.global.service.ICurrentUser;
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
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
/**
 * Attribute-Based Access Control (ABAC) service responsible for authorization
 * decisions across the project management domain.
 *
 * This service evaluates whether the current authenticated user is allowed
 * to perform specific actions on projects and their related resources
 * (epics, user stories, sprints, tasks, and reports).
 */
@RequiredArgsConstructor
@Component("abacService")
public class AbacService implements IAbacService {

    private final ICurrentUser currentUserService;
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
    public boolean canAccessProject(String projectId, String action) {
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
    public boolean canCreateEpic(String projectId) {
        return hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
    }

    @Override
    public boolean canModifyEpic(String projectId, String epicId) {
        return validateOwnershipAndRole(
            projectId, epicId, epicRepository::getProductBackLogIdByEpicId,
            RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);
    }

    @Override
    public boolean canDeleteEpic(String projectId, String epicId) {
        return validateOwnershipAndRole(
            projectId, epicId, epicRepository::getProductBackLogIdByEpicId,
            RoleType.PRODUCT_OWNER);
    }

    // --- User Story Level ---

    @Override
    public boolean canCreateStory(String projectId) {
        return hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
    }

    @Override
    public boolean canModifyStory(String projectId, String storyId) {
        return validateOwnershipAndRole(
            projectId, storyId,
            userStoryRepository::getProductBackLogIdByUserStoryId,
            RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);
    }

    @Override
    public boolean canDeleteStory(String projectId, String storyId) {
        return validateOwnershipAndRole(
            projectId, storyId,
            userStoryRepository::getProductBackLogIdByUserStoryId,
            RoleType.PRODUCT_OWNER);
    }

    @Override
    public boolean canUpdateStoryStatus(String projectId, String sprintId,
                                        String storyId) {
        return validateOwnershipAndRole(
                   projectId, storyId,
                   userStoryRepository::getProductBackLogIdByUserStoryId,
                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
            isSprintMember(sprintId);
    }

    // --- Sprint Level ---

    @Override
    public boolean canCreateSprint(String projectId) {
        return hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                              RoleType.SCRUM_MASTER);
    }

    @Override
    public boolean canManageSprintStories(String projectId, String sprintId) {
        return validateOwnershipAndRole(
                   projectId, sprintId,
                   sprintBackLogRepository::getProductBackLogIdBySprintId,
                   RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
            isSprintMember(sprintId);
    }

    @Override
    public boolean canUpdateSprintStatus(String projectId, String sprintId) {
        return validateOwnershipAndRole(
                   projectId, sprintId,
                   sprintBackLogRepository::getProductBackLogIdBySprintId,
                   RoleType.SCRUM_MASTER) &&
            isSprintMember(sprintId);
    }

    // --- Task Level ---

    @Override
    public boolean canCreateTask(String projectId, String sprintId,
                                 String storyId) {
        boolean isValidHierarchy = false;
        try {
            String spId =
                userStoryRepository.getSprintBackLogIdByUserStoryId(storyId);
            String prId =
                sprintBackLogRepository.getProductBackLogIdBySprintId(sprintId);
            isValidHierarchy = sprintId.equals(spId) && projectId.equals(prId);
        } catch (Exception e) {
            return false;
        }

        return isValidHierarchy &&
            hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                           RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
            isSprintMember(sprintId);
    }

    @Override
    public boolean canUpdateTaskStatus(String projectId, String sprintId,
                                       String taskId) {

        return validateOwnershipAndRole(
                   projectId, taskId,
                   taskRepository::getProductBackLogIdByTaskId,
                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
            isSprintMember(sprintId);
    }

    @Override
    public boolean canAssignTask(String projectId, String sprintId,
                                 String taskId) {

        return validateOwnershipAndRole(
                   projectId, taskId,
                   taskRepository::getProductBackLogIdByTaskId,
                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
            isSprintMember(sprintId);
    }

    // --- Reporting ---

    @Override
    public boolean canViewReport(String projectId, String sprintId) {
        return validateOwnershipAndRole(
            projectId, sprintId,
            sprintBackLogRepository::getProductBackLogIdBySprintId,
            RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER, RoleType.DEVELOPER);
    }

    // --- Helper Methods ---

    private boolean hasProjectRole(String projectId, RoleType... roles) {
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

    private boolean
    validateOwnershipAndRole(String projectId, String resourceId,
                             Function<String, String> idResolver,
                             RoleType... roles) {
        try {
            String resolvedProjectId = idResolver.apply(resourceId);
            if (!projectId.equals(resolvedProjectId))
                return false;
            return hasProjectRole(projectId, roles);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSprintMember(String sprintId) {
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
}
