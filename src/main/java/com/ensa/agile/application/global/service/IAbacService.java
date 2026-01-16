package com.ensa.agile.application.global.service;

public interface IAbacService {
    boolean canCreateProject();
    boolean canAccessProject(String projectId, String action);
    boolean canAccessEpic(String projectId, String epicId, String action);
    boolean canAccessStory(String projectId, String sprintId, String storyId,
                           String action);
    boolean canAccessSprint(String projectId, String sprintId, String action);
    boolean canAccessTask(String projectId, String sprintId, String storyId,
                          String taskId, String action);
    boolean canViewReport(String projectId, String sprintId);
}
