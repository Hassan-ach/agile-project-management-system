package com.ensa.agile.application.story.response;

import java.util.UUID;

import com.ensa.agile.domain.story.enums.StoryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserStoryHistoryResponse {

    private UUID id;
    private StoryStatus status;
    private String note;
}
