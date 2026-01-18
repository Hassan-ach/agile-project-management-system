package com.ensa.agile.application.sprint.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class SprintBurndownChartResponse {
    private UUID sprintId;
    private Double totalStoryPoints;
    private List<SprintBurndownDataPoint> dataPoints;

    public SprintBurndownChartResponse(UUID sprintId, Double totalStoryPoints, List<SprintBurndownDataPoint> dataPoints) {
        this.sprintId = sprintId;
        this.totalStoryPoints = totalStoryPoints;
        this.dataPoints = dataPoints == null ? List.of() : dataPoints;
    }

    public void addDataPoint(LocalDate date, Double idealRemaining, Double actualRemaining) {
        this.dataPoints.add(
            SprintBurndownDataPoint.builder()
                .date(date)
                .idealRemaining(idealRemaining)
                .actualRemaining(actualRemaining)
                .build()
        );
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SprintBurndownDataPoint {
        private LocalDate date;
        private Double idealRemaining;  
        private Double actualRemaining;
    }
}

