package com.ensa.agile.application.sprint.usecase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.response.SprintBackLogProgressResponse;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.enums.StoryStatus;
import com.ensa.agile.domain.story.repository.UserStoryRepository;
import com.ensa.agile.domain.task.entity.Task;
import com.ensa.agile.domain.task.repository.TaskRepository;

@Component
public class GetSprintProgressUseCase extends BaseUseCase<UUID, SprintBackLogProgressResponse> {

    private final SprintBackLogRepository sprintBackLogRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskRepository taskRepository;

    public GetSprintProgressUseCase(
        ITransactionalWrapper tr,
        SprintBackLogRepository sprintBackLogRepository,
        UserStoryRepository userStoryRepository,
        TaskRepository taskRepository
    ) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.userStoryRepository = userStoryRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public SprintBackLogProgressResponse execute(UUID sprintId) {
        var sprint = sprintBackLogRepository.findById(sprintId);
        var allStories = userStoryRepository.findAllBySprintId(sprintId);

        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), sprint.getEndDate());
        long totalDurationDays = ChronoUnit.DAYS.between(sprint.getStartDate(), sprint.getEndDate()) + 1;

        double totalPoints = allStories.stream()
            .mapToDouble(story -> story.getStoryPoints())
            .sum();
        double completedPoints = allStories.stream().filter(story -> story.getStatus().isDone())
            .mapToDouble(story -> story.getStoryPoints())
            .sum();
        double progressPercentage = totalPoints == 0 ? 0 : (completedPoints / totalPoints) * 100;

        Map<StoryStatus, Long> statusCountMap = allStories.stream()
            .collect(Collectors.groupingBy(
            story -> story.getStatus().getStatus(),
            Collectors.counting()
            ));
        
        List<Task> allTasks = taskRepository.findAllBySprintId(sprintId);

        long unassignedTasksCount = allTasks.stream()
            .filter(task -> task.getAssignee() == null)
            .count();
        long totalTasksCount = allTasks.size();


        return SprintBackLogProgressResponse.builder()
            .sprintId(sprint.getId())
            .sprintName(sprint.getName())
            .status(sprint.getStatus().getStatus())
            .daysRemaining(daysRemaining)
            .totalDurationDays(totalDurationDays)
            .totalStoryPoints(totalPoints)
            .completedStoryPoints(completedPoints)
            .progressPercentage(progressPercentage)
            .storiesByStatus(statusCountMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().intValue()
            ))
            )
            .unassignedTasksCount((int) unassignedTasksCount)
            .totalTasksCount((int) totalTasksCount)
            .build();

    }
}
