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
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Rules Definition:
//
// 1. Project Level:
//    - CREATE: Anyone
//    - VIEW / VIEW_BACKLOG: Project Owner, Scrum Master, Developer
//    - INVITE / REMOVE (Member/Developer/Scrum Master): Project Owner
//    - UPDATE / DELETE: Project Owner
//
// 2. Epic Level:
//    - CREATE: Project Owner
//    - VIEW: Project Owner, Scrum Master, Developer
//    - VIEW_ALL: Project Owner, Scrum Master, Developer
//    - UPDATE: Project Owner, Scrum Master
//    - DELETE: Project Owner
//
// 3. User Story Level:
//    - CREATE: Project Owner
//    - VIEW / VIEW_HISTORY: Project Owner, Scrum Master, Developer
//    - UPDATE / UPDATE_PRIORITY: Project Owner, Scrum Master
//    - DELETE / LINK_TO_EPIC / UNLINK_TO_EPIC: Project Owner
//    - UPDATE_STATUS: Scrum Master, Developer (must be sprint member)
//
// 4. Sprint Level:
//    - CREATE: Project Owner, Scrum Master
//    - VIEW / VIEW_BACKLOG: Project Owner, Scrum Master, Developer
//    - UPDATE_STATUS: Scrum Master (must be sprint member)
//    - INVITE_MEMBER: Project Owner, Scrum Master (must be sprint member)
//    - DELETE / UPDATE / MANAGE_STORIES / VIEW_HISTORY / VIEW_BURNDOWN /
//    VIEW_PROGRESS:
//      Project Owner or Scrum Master (if sprint member)
//
// 5. Task Level:
//    - CREATE: Project Owner, Scrum Master, Developer (must be sprint member)
//    - VIEW: Project Owner, Scrum Master, Developer
//    - VIEW_HISTORY: Project Owner or (Scrum Master / Developer if sprint
//    member)
//    - UPDATE: Project Owner, Scrum Master (must be sprint member)
//    - UPDATE_STATUS / ASSIGN / UNASSIGN: Scrum Master, Developer (must be
//    sprint member)
//    - DELETE: Project Owner, Scrum Master (must be sprint member)
//
// 6. Reporting:
//    - SPRINT: Project Owner, Scrum Master, Developer (must belong to sprint)
//    - PROJECT: Project Owner only

@RequiredArgsConstructor
@Component("abacService")
@Transactional
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
    public boolean canAccessProject(UUID projectId, String action) {
        return switch (action) {
            case "CREATE" -> true;
            case "INVITE_MEMBER", "INVITE_DEVELOPER", "INVITE_SCRUM_MASTER" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "VIEW", "VIEW_BACKLOG" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER);
            case "REMOVE_MEMBER", "REMOVE_DEVELOPER", "REMOVE_SCRUM_MASTER" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "UPDATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            case "DELETE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
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

            case "VIEW_ALL" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER,
                                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER);

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
    public boolean canAccessStory(UUID projectId, UUID storyId, String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);

            case "VIEW", "VIEW_HISTORY" ->
                isValideId(storyId) &&
                    validateRole(
                        storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE", "UPDATE_PRIORITY" ->
                isValideId(storyId) &&
                    validateRole(
                        storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER);

            case "DELETE", "LINK_TO_EPIC", "UNLINK_TO_EPIC" ->
                isValideId(storyId) &&
                    validateRole(
                        storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER);

            case "UPDATE_STATUS" ->
                isValideId(storyId) &&
                    validateRole(
                        storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(
                        storyId,
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

            case "VIEW", "VIEW_BACKLOG" ->
                isValideId(sprintId) &&
                    validateRole(
                        sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);

            case "UPDATE_STATUS" ->
                isValideId(sprintId) &&
                    validateRole(
                        sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId, (UUID id) -> { return id; });

            case "INVITE_MEMBER" ->
                isValideId(sprintId) &&
                    validateRole(
                        sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(sprintId, (UUID id) -> { return id; });

            case "DELETE", "UPDATE", "MANAGE_STORIES", "VIEW_HISTORY",
                "VIEW_BURNDOWN", "VIEW_PROGRESS" ->
                isValideId(sprintId) &&
                    (validateRole(
                         sprintId,
                         sprintBackLogRepository::getProductBackLogIdBySprintId,
                         RoleType.PRODUCT_OWNER) ||
                     (validateRole(sprintId,
                                   sprintBackLogRepository::
                                       getProductBackLogIdBySprintId,
                                   RoleType.SCRUM_MASTER) &&
                      isSprintMember(sprintId, (UUID id) -> { return id; })));

            default -> false;
        };
    }

    // --- Task Level ---
    @Override
    public boolean canAccessTask(UUID storyId, UUID taskId, String action) {
        return switch (action) {
            case "CREATE" ->
                isValideId(storyId) &&
                    validateRole(
                        storyId,
                        userStoryRepository::getProductBackLogIdByUserStoryId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER) &&
                    isSprintMember(
                        storyId,
                        userStoryRepository::getSprintBackLogIdByUserStoryId);

            case "VIEW" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                                 taskRepository::getProductBackLogIdByTaskId,
                                 RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                                 RoleType.DEVELOPER);

            case "VIEW_HISTORY" ->
                isValideId(taskId) &&
                    (validateRole(taskId,
                                  taskRepository::getProductBackLogIdByTaskId,
                                  RoleType.PRODUCT_OWNER) ||
                     (validateRole(taskId,
                                   taskRepository::getProductBackLogIdByTaskId,
                                   RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                      isSprintMember(taskId,
                                     taskRepository::getSprintIdByTaskId)));

            case "UPDATE" ->
                isValideId(taskId) &&
                    validateRole(
                        taskId, taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "UPDATE_STATUS" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                                 taskRepository::getProductBackLogIdByTaskId,
                                 RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "ASSIGN" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                                 taskRepository::getProductBackLogIdByTaskId,
                                 RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "UNASSIGN" ->
                isValideId(taskId) &&
                    validateRole(taskId,
                                 taskRepository::getProductBackLogIdByTaskId,
                                 RoleType.SCRUM_MASTER, RoleType.DEVELOPER) &&
                    isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            case "DELETE" ->
                isValideId(taskId) &&
                    validateRole(
                        taskId, taskRepository::getProductBackLogIdByTaskId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER) &&
                    isSprintMember(taskId, taskRepository::getSprintIdByTaskId);

            default -> false;
        };
    }

    // --- Reporting ---
    @Override
    public boolean canViewReport(UUID projectId, UUID sprintId, String action) {
        return switch (action) {
            case "SPRINT" ->
                isValideId(sprintId) &&
                    validateRole(
                        sprintId,
                        sprintBackLogRepository::getProductBackLogIdBySprintId,
                        RoleType.PRODUCT_OWNER, RoleType.SCRUM_MASTER,
                        RoleType.DEVELOPER);
            case "PROJECT" ->
                isValideId(projectId) &&
                    hasProjectRole(projectId, RoleType.PRODUCT_OWNER);
            default -> false;
        };
    }

    // --- Helper Methods ---
    private boolean hasProjectRole(UUID projectId, RoleType... roles) {
        if (projectId == null)
            return false;
        User user = currentUserService.getCurrentUser();
        ProjectMember pm =
            projectMemberRepository.findByUserIdAndProductBackLogId(
                user.getId(), projectId);
        return pm != null && Set.of(roles).contains(pm.getRole());
    }

    private boolean validateRole(UUID resourceId,
                                 Function<UUID, UUID> idResolver,
                                 RoleType... roles) {
        UUID projectId = idResolver.apply(resourceId);
        return hasProjectRole(projectId, roles);
    }

    private boolean isSprintMember(UUID resourceId,
                                   Function<UUID, UUID> idResolver) {
        User currentUser = currentUserService.getCurrentUser();
        UUID sprintId = idResolver.apply(resourceId);
        if (!sprintMembersRepository.existsBySprintBackLogIdAndUserId(
                sprintId, currentUser.getId())) {
            return false;
        }
        return true;
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
