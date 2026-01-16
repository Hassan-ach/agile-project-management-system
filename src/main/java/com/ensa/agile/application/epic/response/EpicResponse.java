package com.ensa.agile.application.epic.response;

import com.ensa.agile.application.story.response.UserStoryResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class EpicResponse {
    private UUID id;
    private final String title;
    private final String description;

    private UUID createdBy;
    private LocalDateTime createdDate;
    private UUID lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private List<UserStoryResponse> userStories;
}
