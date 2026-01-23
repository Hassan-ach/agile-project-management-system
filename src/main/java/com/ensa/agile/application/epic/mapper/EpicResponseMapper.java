package com.ensa.agile.application.epic.mapper;

import com.ensa.agile.application.epic.response.EpicResponse;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.domain.epic.entity.Epic;
import java.util.function.BiConsumer;

public class EpicResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static EpicResponse toResponse(Epic domain) {
        if (domain == null)
            return null;

        return EpicResponse.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: EpicResponseMapper.toResponse(epic,
     * EpicResponseMapper::attachUserStories);
     */
    @SafeVarargs
    public static EpicResponse
    toResponse(Epic domain, BiConsumer<EpicResponse, Epic>... enrichers) {
        EpicResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<EpicResponse, Epic> enricher : enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachUserStories(EpicResponse response, Epic domain) {
        if (domain.getUserStories() != null) {
            response.setUserStories(
                domain.getUserStories()
                    .stream()
                    .map(UserStoryResponseMapper::toResponse)
                    .toList());
        }
    }
}
