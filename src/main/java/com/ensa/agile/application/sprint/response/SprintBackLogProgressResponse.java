package com.ensa.agile.application.sprint.response;

import java.util.Map;
import java.util.UUID;

import com.ensa.agile.domain.sprint.enums.SprintStatus;
import com.ensa.agile.domain.story.enums.StoryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SprintBackLogProgressResponse {
    private UUID sprintId;
    private String sprintName;
    private SprintStatus status;          
    
    private long daysRemaining;
    private long totalDurationDays;
    
    private Double totalStoryPoints;
    private Double completedStoryPoints;
    private Double progressPercentage; 
    
    private Map<StoryStatus, Integer> storiesByStatus;
    
    private int unassignedTasksCount;
    private int totalTasksCount;
}
