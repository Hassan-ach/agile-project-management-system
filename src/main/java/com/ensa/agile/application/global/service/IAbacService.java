package com.ensa.agile.application.global.service;

public interface IAbacService {

    // --- Project Level ---
    boolean canCreateProject();
    boolean canAccessProject(String projectId, String action);

    // --- Epic Level ---
    boolean canCreateEpic(String projectId);
    boolean canModifyEpic(String projectId, String epicId);
    boolean canDeleteEpic(String projectId, String epicId);

    // --- User Story Level ---
    boolean canCreateStory(String projectId);
    boolean canModifyStory(String projectId, String storyId);
    boolean canDeleteStory(String projectId, String storyId);
    boolean canUpdateStoryStatus(String projectId, String sprintId,
                                 String storyId);

    // --- Sprint Level ---
    boolean canCreateSprint(String projectId);
    boolean canManageSprintStories(String projectId, String sprintId);
    boolean canUpdateSprintStatus(String projectId, String sprintId);

    // --- Task Level ---
    boolean canCreateTask(String projectId, String sprintId, String storyId);
    boolean canUpdateTaskStatus(String projectId, String sprintId,
                                String taskId);
    boolean canAssignTask(String projectId, String sprintId, String taskId);

    // --- Reporting ---
    boolean canViewReport(String projectId, String sprintId);
}
