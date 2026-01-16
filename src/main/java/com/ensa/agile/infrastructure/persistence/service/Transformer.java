package com.ensa.agile.infrastructure.persistence.service;

import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.product.response.ProjectMemberResponse;
import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.sprint.response.SprintMemberResponse;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.application.task.response.TaskResponse;
import com.ensa.agile.domain.product.enums.MemberStatus;
import com.ensa.agile.domain.product.enums.RoleType;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import com.ensa.agile.domain.story.enums.MoscowType;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.task.entity.TaskHistory;
import com.ensa.agile.domain.task.enums.TaskStatus;
import com.ensa.agile.infrastructure.persistence.jpa.global.join.Row;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Transformer {

    public static ProductBackLogResponse transform(List<Row> rows) {
        if (rows == null || rows.isEmpty())
            return null;

        // 1. Root Object Initialization
        // If Product data is missing (sparse row), we return a skeleton
        // response that holds the requested entity (Sprint, Epic, or Story).
        Row firstRow = rows.get(0);
        ProductBackLogResponse response =
            firstRow.getProductId() != null
                ? mapBacklog(firstRow)
                : ProductBackLogResponse.builder().build();

        // 2. Identity Maps
        Map<String, ProjectMemberResponse> membersMap = new LinkedHashMap<>();
        Map<String, SprintBackLogResponse> sprintsMap = new LinkedHashMap<>();
        Map<String, EpicResponse> epicsMap = new LinkedHashMap<>();
        Map<String, UserStoryResponse> storiesMap = new LinkedHashMap<>();
        Map<String, List<UserStoryResponse>> epicStories = new HashMap<>();

        for (Row row : rows) {
            // Process Project Member
            if (row.getProjectMemberId() != null) {
                membersMap.computeIfAbsent(row.getProjectMemberId(),
                                           id -> mapMember(row));
            }

            // Process Sprint
            if (row.getSprintId() != null) {
                SprintBackLogResponse sprint = sprintsMap.computeIfAbsent(
                    row.getSprintId(), id -> mapSprint(row));

                if (row.getSprintMemberId() != null) {
                    if (sprint.getMembers().stream().noneMatch(
                            m
                            -> m.getId().toString().equals(
                                row.getSprintMemberId()))) {
                        sprint.getMembers().add(mapSprintMember(row));
                    }
                }
            }

            // Process Epic
            if (row.getEpicId() != null) {
                epicsMap.computeIfAbsent(row.getEpicId(), id -> mapEpic(row));
            }

            // Process User Story
            if (row.getStoryId() != null) {
                UserStoryResponse story = storiesMap.computeIfAbsent(
                    row.getStoryId(), id -> mapStory(row));

                // Link to Epic Map if the row shows this story belongs to an
                // epic
                if (row.getEpicId() != null) {
                    List<UserStoryResponse> list = epicStories.computeIfAbsent(
                        row.getEpicId(), k -> new ArrayList<>());
                    if (list.stream().noneMatch(
                            s -> s.getId().equals(story.getId()))) {
                        list.add(story);
                    }
                }

                // Process Task
                if (row.getTaskId() != null) {
                    if (story.getTasks().stream().noneMatch(
                            t
                            -> t.getId().toString().equals(row.getTaskId()))) {
                        story.getTasks().add(mapTask(row));
                    }
                }
            }
        }

        // 3. Assemble Hierarchy
        response.setProjectMembers(new ArrayList<>(membersMap.values()));
        response.setSprintBackLogs(new ArrayList<>(sprintsMap.values()));

        // Attach Stories to Epics
        epicsMap.forEach((epicId, epic) -> {
            epic.setUserStories(
                epicStories.getOrDefault(epicId, new ArrayList<>()));
        });
        response.setEpics(new ArrayList<>(epicsMap.values()));

        // 4. Resolve Orphan/Root Stories
        // A story is added to the root list if it appears in at least one row
        // where epicId is null. This handles PRODUCT, SPRINT, and STORY targets
        // automatically.
        List<UserStoryResponse> rootStories =
            storiesMap.values()
                .stream()
                .filter(s
                        -> rows.stream().anyMatch(
                            r
                            -> r.getStoryId().equals(s.getId().toString()) &&
                                   r.getEpicId() == null))
                .collect(Collectors.toList());

        response.setUserStories(rootStories);

        return response;
    }

    private static ProductBackLogResponse mapBacklog(Row row) {
        return ProductBackLogResponse.builder()
            .id(UUID.fromString(row.getProductId()))
            .name(row.getProductName())
            .description(row.getProductDescription())
            .createdBy(UUID.fromString(row.getProductCreatedBy()))
            .createdDate(row.getProductCreatedDate())
            .lastModifiedBy(UUID.fromString(row.getProductLastModifiedBy()))
            .lastModifiedDate(row.getProductLastModifiedDate())
            .build();
    }

    private static ProjectMemberResponse mapMember(Row row) {
        ProjectMemberResponse member =
            ProjectMemberResponse.builder()
                .memberId(UUID.fromString(row.getProjectMemberId()))
                .userEmail(row.getProjectMemberEmail())
                .role(RoleType.valueOf(row.getProjectMemberRole()))
                .status(MemberStatus.valueOf(row.getProjectMemberStatus()))
                .invitedBy(UUID.fromString(row.getProjectMemberInvitedBy()))
                .invitationDate(row.getProjectMemberInvitationDate())
                .build();
        return member;
    }

    private static SprintBackLogResponse mapSprint(Row row) {

        SprintBackLogResponse sprint =
            SprintBackLogResponse.builder()
                .id(UUID.fromString(row.getSprintId()))
                .name(row.getSprintName())
                .scrumMasterEmail(row.getSprintScrumMasterEmail())
                .startDate(row.getSprintStartDate())
                .endDate(row.getSprintEndDate())
                .goal(row.getSprintGoal())
                .createdBy(UUID.fromString(row.getSprintCreatedBy()))
                .createdDate(row.getSprintCreatedDate())
                .build();

        // Map Sprint History (Latest)
        if (row.getSprintHistoryId() != null) {
            sprint.setStatus(
                SprintHistory.builder()
                    .id(UUID.fromString(row.getSprintHistoryId()))
                    .status(SprintStatus.valueOf(row.getSprintHistoryStatus()))
                    .note(row.getSprintHistoryNote())
                    .build());
        }

        sprint.setMembers(new ArrayList<>());
        sprint.setUserStories(new ArrayList<>());
        return sprint;
    }

    private static SprintMemberResponse mapSprintMember(Row row) {

        SprintMemberResponse sm =
            SprintMemberResponse.builder()
                .id(UUID.fromString(row.getSprintMemberId()))
                .userEmail(row.getSprintMemberEmail())
                .invitedBy(UUID.fromString(row.getSprintMemberInvitedBy()))
                .joinedAt(row.getSprintMemberJoinedAt())
                .build();
        return sm;
    }

    private static EpicResponse mapEpic(Row row) {
        EpicResponse epic =
            EpicResponse.builder()
                .id(UUID.fromString(row.getEpicId()))
                .title(row.getEpicTitle())
                .description(row.getEpicDescription())
                .createdBy(UUID.fromString(row.getEpicCreatedBy()))
                .createdDate(row.getEpicCreatedDate())
                .userStories(new ArrayList<>())
                .build();
        return epic;
    }

    private static UserStoryResponse mapStory(Row row) {
        UserStoryResponse story =
            UserStoryResponse.builder()
                .id(UUID.fromString(row.getStoryId()))
                .title(row.getStoryTitle())
                .description(row.getStoryDescription())
                .priority(MoscowType.valueOf(row.getStoryPriority()))
                .storyPoints(row.getStoryPoints())
                .acceptanceCriteria(row.getStoryAcceptanceCriteria())
                .createdBy(UUID.fromString(row.getStoryCreatedBy()))
                .createdDate(row.getStoryCreatedDate())
                .tasks(new ArrayList<>())
                .build();

        // Map Story History (Latest)
        if (row.getStoryHistoryId() != null) {
            story.setStatus(
                UserStoryHistory.builder()
                    .id(UUID.fromString(row.getStoryHistoryId()))
                    .status(StoryStatus.valueOf(row.getStoryHistoryStatus()))
                    .note(row.getStoryHistoryNote())
                    .build());
        }

        story.setTasks(new ArrayList<>());
        return story;
    }

    private static TaskResponse mapTask(Row row) {
        TaskResponse task =
            TaskResponse.builder()
                .id(UUID.fromString(row.getTaskId()))
                .title(row.getTaskTitle())
                .description(row.getTaskDescription())
                .assignee(row.getTaskAssignee())
                .estimatedHours(row.getTaskEstimatedHours())
                .actualHours(row.getTaskActualHours())
                .createdBy(UUID.fromString(row.getTaskCreatedBy()))
                .createdDate(row.getTaskCreatedDate())
                .build();

        // Map Task History (Latest)
        if (row.getTaskHistoryId() != null) {
            task.setStatus(
                TaskHistory.builder()
                    .id(UUID.fromString(row.getTaskHistoryId()))
                    .status(TaskStatus.valueOf(row.getTaskHistoryStatus()))
                    .note(row.getTaskHistoryNote())
                    .build());
        }
        return task;
    }
}
