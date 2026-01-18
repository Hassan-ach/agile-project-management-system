package com.ensa.agile.application.sprint.usecase;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ensa.agile.application.global.transaction.ITransactionalWrapper;
import com.ensa.agile.application.global.usecase.BaseUseCase;
import com.ensa.agile.application.sprint.response.SprintBurndownChartResponse;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import com.ensa.agile.domain.sprint.repository.SprintBackLogRepository;
import com.ensa.agile.domain.story.entity.UserStory;
import com.ensa.agile.domain.story.repository.UserStoryRepository;

@Component
public class GetSprintBurndownChartUseCase extends BaseUseCase<UUID, SprintBurndownChartResponse> {

    private final SprintBackLogRepository sprintBackLogRepository;
    private final UserStoryRepository userStoryRepository;
    
    public GetSprintBurndownChartUseCase(ITransactionalWrapper tr,
                                        UserStoryRepository userStoryRepository,
                                        SprintBackLogRepository sprintBackLogRepository) {
        super(tr);
        this.sprintBackLogRepository = sprintBackLogRepository;
        this.userStoryRepository = userStoryRepository;
    }

@Override
public SprintBurndownChartResponse execute(UUID sprintId) {
    SprintBackLog sprint = sprintBackLogRepository.findById(sprintId);
    
    List<UserStory> allStories = userStoryRepository.findAllBySprintId(sprintId);

    double totalSprintPoints = allStories.stream()
        .mapToDouble(UserStory::getStoryPoints)
        .sum();

    SprintBurndownChartResponse response = SprintBurndownChartResponse.builder()
        .sprintId(sprint.getId())
        .totalStoryPoints(totalSprintPoints)
        .build();

    long totalDays = ChronoUnit.DAYS.between(sprint.getStartDate(), sprint.getEndDate()) + 1;
    double idealBurnRate = totalSprintPoints / (double) (totalDays);
    
    // Stream of dates from Start to End
    List<LocalDate> sprintDates = sprint.getStartDate()
        .datesUntil(sprint.getEndDate().plusDays(1)) 
        .collect(Collectors.toList());

    for (int dayIndex = 0; dayIndex < sprintDates.size(); dayIndex++) {
        LocalDate currentDate = sprintDates.get(dayIndex);

        double pointsCompletedByDate = allStories.stream()
            .filter(story -> isStoryDoneByDate(story, currentDate)) 
            .mapToDouble(UserStory::getStoryPoints)
            .sum();

        double actualRemaining = totalSprintPoints - pointsCompletedByDate;

        double idealRemaining = Math.max(0, totalSprintPoints - (idealBurnRate * (dayIndex + 1)));

        response.addDataPoint(currentDate, idealRemaining, actualRemaining);
    }

    return response;
}

private boolean isStoryDoneByDate(UserStory story, LocalDate targetDate) {
    // Must be currently DONE
    if (!story.getStatus().isDone()) {
        return false;
    }
    LocalDate completionDate = story.getCreatedDate().toLocalDate();

    return !completionDate.isAfter(targetDate);
}}
