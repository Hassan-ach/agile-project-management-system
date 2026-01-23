package com.ensa.agile.application.story.mapper;
import com.ensa.agile.application.story.response.UserStoryResponse;
import com.ensa.agile.domain.story.entity.UserStory;
import java.util.function.BiConsumer;

public class UserStoryResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserStoryResponse toResponse(UserStory domain) {
        if (domain == null)
            return null;

        return UserStoryResponse.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .description(domain.getDescription())
            .priority(domain.getPriority())
            .storyPoints(domain.getStoryPoints())
            .acceptanceCriteria(domain.getAcceptanceCriteria())
            .status(domain.getStatus() != null ? domain.getStatus().getStatus()
                                               : null)
            .latestHistory(
                UserStoryHistoryResponseMapper.toResponse(domain.getStatus()))

            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: UserStoryResponseMapper.toResponse(domain,
     * UserStoryResponseMapper::attachTasks);
     */
    @SafeVarargs
    public static UserStoryResponse
    toResponse(UserStory domain,
               BiConsumer<UserStoryResponse, UserStory>... enrichers) {
        UserStoryResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<UserStoryResponse, UserStory> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    /*
    public static void attachTasks(UserStoryResponse response, UserStory domain)
    { if (domain.getTasks() != null) { response.setTasks(
                domain.getTasks().stream()
                    .map(TaskResponseMapper::toResponse)
                    .toList()
            );
        }
    }
    */
}
