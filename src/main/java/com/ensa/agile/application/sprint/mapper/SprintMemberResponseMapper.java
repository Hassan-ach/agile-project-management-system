package com.ensa.agile.application.sprint.mapper;

import com.ensa.agile.application.sprint.response.SprintMemberResponse;
import com.ensa.agile.domain.sprint.entity.SprintMember;
import java.util.function.BiConsumer;

public class SprintMemberResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     * User email is left null to avoid accessing potential lazy loaded
     * entities.
     */
    public static SprintMemberResponse toResponse(SprintMember domain) {
        if (domain == null)
            return null;

        return SprintMemberResponse.builder()
            .id(domain.getId())
            // Audit Metadata mapped to Join details
            .invitedBy(domain.getCreatedBy())
            .joinedAt(domain.getCreatedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: SprintMemberResponseMapper.toResponse(domain,
     * SprintMemberResponseMapper::attachUserEmail);
     */
    @SafeVarargs
    public static SprintMemberResponse
    toResponse(SprintMember domain,
               BiConsumer<SprintMemberResponse, SprintMember>... enrichers) {
        SprintMemberResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<SprintMemberResponse, SprintMember> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachUserEmail(SprintMemberResponse response,
                                       SprintMember domain) {
        if (domain.getUser() != null) {
            response.setUserEmail(domain.getUser().getEmail());
        }
    }
}
