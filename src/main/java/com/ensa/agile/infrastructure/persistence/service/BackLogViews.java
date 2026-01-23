package com.ensa.agile.infrastructure.persistence.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// Define all projections in one place for easy management
public class BackLogViews {
    public interface ProjectView {
        UUID getId();
        String getName();
        String getDescription();
        UUID getCreatedBy();
        LocalDateTime getCreatedDate();
    }

    public interface SprintView {
        UUID getId();
        String getName();
        String getGoal();
        LocalDate getStartDate();
        LocalDate getEndDate();
        UUID getCreatedBy();
        LocalDateTime getCreatedDate();
        String getScrumMasterEmail(); // Flattened join
        String getLatestStatus();     // Calculated via Lateral Join
    }

    public interface EpicView {
        UUID getId();
        String getTitle();
        String getDescription();
        UUID getCreatedBy();
        LocalDateTime getCreatedDate();
    }

    public interface MemberView {
        UUID getId();
        String getEmail();
        String getRole();
        String getStatus();
        UUID getInvitedBy();
        LocalDateTime getInvitationDate();
    }

    public interface SprintMemberView {
        UUID getId();
        String getUserEmail();
        UUID getInvitedBy();
        LocalDateTime getJoinedAt();
    }

    public interface StoryView {
        UUID getId();
        String getTitle();
        String getDescription();
        String getPriority();
        Integer getStoryPoints();
        String getAcceptanceCriteria();
        UUID getCreatedBy();
        LocalDateTime getCreatedDate();

        // Foreign Keys for Stitching
        UUID getSprintId();
        UUID getEpicId();

        // Flattened History
        String getLatestStatus();
    }

    public interface TaskView {
        UUID getId();
        String getTitle();
        String getDescription();
        Double getEstimatedHours();
        Double getActualHours();
        UUID getCreatedBy();
        LocalDateTime getCreatedDate();

        // Foreign Keys & Joins
        UUID getUserStoryId();
        UUID getAssigneeId();
        String getAssigneeEmail(); // Joined column

        // Flattened History
        String getLatestStatus();
    }
}
