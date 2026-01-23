package com.ensa.agile.application.sprint.mapper;

import com.ensa.agile.application.sprint.response.SprintBackLogResponse;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.domain.sprint.entity.SprintBackLog;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SprintBackLogResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static SprintBackLogResponse toResponse(SprintBackLog domain) {
        if (domain == null)
            return null;

        return SprintBackLogResponse.builder()
            .id(domain.getId())
            .name(domain.getName())
            .startDate(domain.getStartDate())
            .endDate(domain.getEndDate())
            .goal(domain.getGoal())
            .status(domain.getStatus() != null ? domain.getStatus().getStatus()
                                               : null)
            .latestHistory(
                SprintHistoryResponseMapper.toResponse(domain.getStatus()))

            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .members(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: SprintBackLogResponseMapper.toResponse(domain,
     * SprintBackLogResponseMapper::attachUserStories);
     */
    @SafeVarargs
    public static SprintBackLogResponse
    toResponse(SprintBackLog domain,
               BiConsumer<SprintBackLogResponse, SprintBackLog>... enrichers) {
        SprintBackLogResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<SprintBackLogResponse, SprintBackLog> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachUserStories(SprintBackLogResponse response,
                                         SprintBackLog domain) {
        if (domain.getUserStories() != null) {
            response.setUserStories(
                domain.getUserStories()
                    .stream()
                    .map(UserStoryResponseMapper::toResponse)
                    .toList());
        }
    }

    public static void attachMembers(SprintBackLogResponse response,
                                     SprintBackLog domain) {
        if (domain.getMembers() != null) {
            response.setMembers(domain.getMembers()
                                    .stream()
                                    .map(SprintMemberResponseMapper::toResponse)
                                    .toList());
        }
    }

    // public static void attachMembers(SprintBackLogResponse response,
    //                                  SprintBackLog domain) {
    //     // Assuming domain.getMembers() returns List<SprintMember> and
    //     // SprintMemberResponseMapper exists
    //     if (domain.getMembers() != null) {
    //         response.setMembers(domain.getMembers()
    //                                 .stream()
    //                                 .map(SprintMemberResponseMapper::toResponse)
    //                                 .toList());
    //     }
    // }
}
