package com.ensa.agile.infrastructure.persistence.service;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.epic.request.EpicGetRequest;
import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.global.service.IFetchService;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.product.request.ProductBackLogGetRequest;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.product.response.ProjectMemberResponse;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.application.sprint.request.SprintBackLogGetRequest;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.sprint.response.SprintMemberResponse;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.application.story.request.UserStoryGetRequest;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.EpicView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.MemberView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.SprintMemberView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.SprintView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.StoryView;
import com.ensa.agile.infrastructure.persistence.service.BackLogViews.TaskView;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchService implements IFetchService {

    private final BackLogRepo repo;

    // =========================================================================
    //  PRODUCT FETCH
    // =========================================================================
    @Override
    public ProductBackLogResponse getResponse(ProductBackLogGetRequest req) {
        FetchPlan plan = FetchPlan.resolve(req.getFields());
        UUID productId = req.getId();

        // 1. Fetch Root
        var productEntity = repo.findProductHeader(productId);
        if (productEntity == null)
            throw new ProductBackLogNotFoundException();

        // 2. Parallel-ready fetches based on plan
        var sprints = plan.isSprints() ? repo.findSprintsByProduct(productId)
                                       : List.<SprintView>of();
        var epics = plan.isEpics() ? repo.findEpicsByProduct(productId)
                                   : List.<EpicView>of();
        var stories = plan.isStories() ? repo.findStoriesByProduct(productId)
                                       : List.<StoryView>of();
        var tasks = plan.isTasks() ? repo.findTasksByProduct(productId)
                                   : List.<TaskView>of();
        var members = plan.isMembers() ? repo.findMembersByProduct(productId)
                                       : List.<MemberView>of();

        // 3. Map & Group (Stitching)

        // Group Tasks by Story ID
        Map<UUID, List<TaskResponse>> tasksByStoryId =
            tasks.stream().collect(Collectors.groupingBy(
                TaskView::getUserStoryId,
                Collectors.mapping(this::mapTask, Collectors.toList())));

        Map<UUID, UserStoryResponse> storiesById = stories.stream().collect(
            Collectors.toMap(StoryView::getId, this::mapStory));

        // Group Stories by Sprint ID and Epic ID
        Map<UUID, List<UserStoryResponse>> storiesBySprintId =
            stories.stream()
                .filter(s -> s.getSprintId() != null)
                .collect(Collectors.groupingBy(
                    StoryView::getSprintId, Collectors.mapping(view -> {
                        UserStoryResponse resp = storiesById.get(view.getId());
                        resp.setTasks(tasksByStoryId.getOrDefault(view.getId(),
                                                                  List.of()));
                        return resp;
                    }, Collectors.toList())));

        Map<UUID, List<UserStoryResponse>> storiesByEpicId =
            stories.stream().collect(Collectors.groupingBy(
                StoryView::getEpicId, Collectors.mapping(view -> {
                    UserStoryResponse resp = storiesById.get(view.getId());
                    resp.setTasks(
                        tasksByStoryId.getOrDefault(view.getId(), List.of()));
                    return resp;
                }, Collectors.toList())));

        // 4. Assemble Final Response
        ProductBackLogResponse response = new ProductBackLogResponse();
        response.setId(productEntity.getId());
        response.setName(productEntity.getName());
        response.setDescription(productEntity.getDescription());
        response.setCreatedBy(productEntity.getCreatedBy());
        response.setCreatedDate(productEntity.getCreatedDate());

        if (plan.isMembers()) {
            response.setProjectMembers(
                members.stream().map(this::mapMember).toList());
        }

        if (plan.isSprints()) {
            response.setSprintBackLogs(
                sprints.stream()
                    .map(view -> {
                        var sprintResp = mapSprint(view);
                        sprintResp.setUserStories(
                            storiesBySprintId.getOrDefault(view.getId(),
                                                           List.of()));
                        return sprintResp;
                    })
                    .toList());
        }

        if (plan.isEpics()) {
            response.setEpics(epics.stream()
                                  .map(view -> {
                                      var epicResp = mapEpic(view);
                                      epicResp.setUserStories(
                                          storiesByEpicId.getOrDefault(
                                              view.getId(), List.of()));
                                      return epicResp;
                                  })
                                  .toList());
        }

        if (plan.isStories()) {
            response.setUserStories(storiesById.values().stream().toList());
        }

        return response;
    }

    // =========================================================================
    //  SPRINT FETCH
    // =========================================================================
    @Override
    public SprintBackLogResponse getResponse(SprintBackLogGetRequest req) {
        FetchPlan plan = FetchPlan.resolve(req.getFields());
        UUID sprintId = req.getId();

        // 1. Fetch Data
        var sprintView = repo.findSprintHeader(sprintId);
        if (sprintView == null)
            throw new SprintBackLogNotFoundException();

        var members = (plan.isMembers() || plan.isSprintMembers())
                          ? repo.findMembersBySprint(sprintId)
                          : List.<SprintMemberView>of();
        var stories = plan.isStories() ? repo.findStoriesBySprint(sprintId)
                                       : List.<StoryView>of();

        var tasks = plan.isTasks() ? repo.findTasksBySprint(sprintId)
                                   : List.<TaskView>of();

        // 2. Stitch Tasks -> Stories
        Map<UUID, List<TaskResponse>> tasksByStoryId =
            tasks.stream().collect(Collectors.groupingBy(
                TaskView::getUserStoryId,
                Collectors.mapping(this::mapTask, Collectors.toList())));

        List<UserStoryResponse> storyResponses =
            stories.stream()
                .map(view -> {
                    var resp = mapStory(view);
                    resp.setTasks(
                        tasksByStoryId.getOrDefault(view.getId(), List.of()));
                    return resp;
                })
                .toList();

        // 3. Assemble Response
        SprintBackLogResponse response = mapSprint(sprintView);
        if (plan.isMembers() || plan.isSprintMembers()) {
            response.setMembers(
                members.stream().map(this::mapSprintMember).toList());
        }
        if (plan.isStories()) {
            response.setUserStories(storyResponses);
        }

        return response;
    }

    // =========================================================================
    //  EPIC FETCH
    // =========================================================================
    @Override
    public EpicResponse getResponse(EpicGetRequest req) {
        FetchPlan plan = FetchPlan.resolve(req.getFields());
        UUID epicId = req.getId();

        // 1. Fetch Data
        var epicView = repo.findEpicHeader(epicId);
        if (epicView == null)
            throw new EpicNotFoundException();

        var stories = repo.findStoriesByEpic(epicId);
        var tasks =
            plan.isTasks() ? repo.findTasksByEpic(epicId) : List.<TaskView>of();
        ;

        // 2. Stitch Tasks -> Stories
        Map<UUID, List<TaskResponse>> tasksByStoryId =
            tasks.stream().collect(Collectors.groupingBy(
                TaskView::getUserStoryId,
                Collectors.mapping(this::mapTask, Collectors.toList())));

        List<UserStoryResponse> storyResponses =
            stories.stream()
                .map(view -> {
                    var resp = mapStory(view);
                    resp.setTasks(
                        tasksByStoryId.getOrDefault(view.getId(), List.of()));
                    return resp;
                })
                .toList();

        // 3. Assemble Response
        EpicResponse response = mapEpic(epicView);
        if (plan.isStories()) {
            response.setUserStories(storyResponses);
        }
        return response;
    }

    // =========================================================================
    //  USER STORY FETCH
    // =========================================================================
    @Override
    public UserStoryResponse getResponse(UserStoryGetRequest req) {
        UUID storyId = req.getId();

        var storyView = repo.findStoryHeader(storyId);
        if (storyView == null)
            throw new UserStoryNotFoundException();

        var tasks = repo.findTasksByStory(storyId);

        UserStoryResponse response = mapStory(storyView);
        response.setTasks(tasks.stream().map(this::mapTask).toList());

        return response;
    }

    // =========================================================================
    //  INTERNAL MAPPERS
    // =========================================================================

    private SprintBackLogResponse mapSprint(SprintView view) {
        if (view == null)
            return null;
        SprintBackLogResponse resp = new SprintBackLogResponse();
        resp.setId(view.getId());
        resp.setName(view.getName());
        resp.setGoal(view.getGoal());
        resp.setStartDate(view.getStartDate());
        resp.setEndDate(view.getEndDate());
        resp.setCreatedBy(view.getCreatedBy());
        resp.setCreatedDate(view.getCreatedDate());
        resp.setScrumMasterEmail(view.getScrumMasterEmail());

        // Enum Conversion
        if (view.getLatestStatus() != null) {
            resp.setStatus(
                SprintStatus.valueOf(view.getLatestStatus().toUpperCase()));
        }
        return resp;
    }

    private EpicResponse mapEpic(EpicView view) {
        EpicResponse resp = new EpicResponse();
        resp.setId(view.getId());
        resp.setTitle(view.getTitle());
        resp.setDescription(view.getDescription());
        resp.setCreatedBy(view.getCreatedBy());
        resp.setCreatedDate(view.getCreatedDate());
        return resp;
    }

    private UserStoryResponse mapStory(StoryView view) {
        if (view == null)
            return null;
        UserStoryResponse resp = new UserStoryResponse();
        resp.setId(view.getId());
        resp.setTitle(view.getTitle());
        resp.setDescription(view.getDescription());
        resp.setStoryPoints(view.getStoryPoints());
        resp.setAcceptanceCriteria(view.getAcceptanceCriteria());
        resp.setCreatedBy(view.getCreatedBy());
        resp.setCreatedDate(view.getCreatedDate());

        if (view.getPriority() != null) {
            resp.setPriority(MoscowType.valueOf(view.getPriority()));
        }
        if (view.getLatestStatus() != null) {
            resp.setStatus(
                StoryStatus.valueOf(view.getLatestStatus().toUpperCase()));
        }
        return resp;
    }

    private TaskResponse mapTask(TaskView view) {
        if (view == null)
            return null;
        TaskResponse resp = new TaskResponse();
        resp.setId(view.getId());
        resp.setTitle(view.getTitle());
        resp.setDescription(view.getDescription());
        resp.setEstimatedHours(view.getEstimatedHours());
        resp.setAssignee(view.getAssigneeEmail());

        if (view.getActualHours() != null) {
            resp.setActualHours(view.getActualHours());
        }
        if (view.getLatestStatus() != null) {
            resp.setStatus(TaskStatus.valueOf(view.getLatestStatus()));
        }
        return resp;
    }

    private ProjectMemberResponse mapMember(MemberView view) {
        if (view == null)
            return null;
        ProjectMemberResponse resp = new ProjectMemberResponse();
        resp.setMemberId(view.getId());
        resp.setUserEmail(view.getEmail());
        resp.setInvitedBy(view.getInvitedBy());
        resp.setInvitationDate(view.getInvitationDate());

        if (view.getRole() != null)
            resp.setRole(RoleType.valueOf(view.getRole()));
        if (view.getStatus() != null)
            resp.setStatus(MemberStatus.valueOf(view.getStatus()));

        return resp;
    }

    private SprintMemberResponse mapSprintMember(SprintMemberView view) {
        if (view == null)
            return null;
        SprintMemberResponse resp = new SprintMemberResponse();
        resp.setId(view.getId());
        resp.setUserEmail(view.getUserEmail());
        resp.setInvitedBy(view.getInvitedBy());
        resp.setJoinedAt(view.getJoinedAt());
        return resp;
    }
}
