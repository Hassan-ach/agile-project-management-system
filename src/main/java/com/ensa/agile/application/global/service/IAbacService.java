package com.ensa.agile.application.global.service;

import java.util.UUID;

public interface IAbacService {
    boolean canAccessProject(UUID projectId, String action);
    boolean canAccessEpic(UUID projectId, UUID epicId, String action);
    boolean canAccessStory(UUID projectId,UUID storyId,
                           String action);
    boolean canAccessSprint(UUID projectId, UUID sprintId, String action);
    boolean canAccessTask(UUID storyId,
                          UUID taskId, String action);
    boolean canViewReport(UUID projectId, UUID sprintId, String action);
}
