package com.ensa.agile.application.sprint.mapper;

import com.ensa.agile.application.sprint.response.SprintHistoryResponse;
import com.ensa.agile.domain.sprint.entity.SprintHistory;
import java.util.function.BiConsumer;

public class SprintHistoryResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict fields only.
     * Relationships are excluded by default.
     */
    public static SprintHistoryResponse toResponse(SprintHistory domain) {
        if (domain == null)
            return null;

        return SprintHistoryResponse.builder()
            .id(domain.getId())
            .status(domain.getStatus())
            .note(domain.getNote())
            .build();
    }

    /**
     * 2. Orchestrator: Base mapping + optional enrichers.
     */
    @SafeVarargs
    public static SprintHistoryResponse
    toResponse(SprintHistory domain,
               BiConsumer<SprintHistoryResponse, SprintHistory>... enrichers) {

        SprintHistoryResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<SprintHistoryResponse, SprintHistory> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // ========================================================================
    // 3. Attachers
    // ========================================================================
}
