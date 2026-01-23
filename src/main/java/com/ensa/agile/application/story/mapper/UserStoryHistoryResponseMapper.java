package com.ensa.agile.application.story.mapper;

import com.ensa.agile.application.story.response.UserStoryHistoryResponse;
import com.ensa.agile.domain.story.entity.UserStoryHistory;
import java.util.function.BiConsumer;

public class UserStoryHistoryResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict fields only.
     * Relationships are excluded by default.
     */
    public static UserStoryHistoryResponse toResponse(UserStoryHistory domain) {
        if (domain == null)
            return null;

        return UserStoryHistoryResponse.builder()
            .id(domain.getId())
            .status(domain.getStatus())
            .note(domain.getNote())
            .build();
    }

    /**
     * 2. Orchestrator: Base mapping + optional enrichers.
     */
    @SafeVarargs
    public static UserStoryHistoryResponse toResponse(
        UserStoryHistory domain,
        BiConsumer<UserStoryHistoryResponse, UserStoryHistory>... enrichers) {

        UserStoryHistoryResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<UserStoryHistoryResponse, UserStoryHistory>
                     enricher : enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // ========================================================================
    // 3. Attachers (add only when needed)
    // ========================================================================
}
