package com.ensa.agile.application.product.mapper;

import com.ensa.agile.application.product.response.ProjectMemberResponse;
import com.ensa.agile.domain.product.entity.ProjectMember;
import java.util.function.BiConsumer;

public class ProjectMemberResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     * User email is left null to avoid accessing potential lazy loaded
     * entities.
     */
    public static ProjectMemberResponse toResponse(ProjectMember domain) {
        if (domain == null)
            return null;

        return ProjectMemberResponse.builder()
            .memberId(domain.getId())
            .role(domain.getRole())
            .status(domain.getStatus())
            // Audit Metadata mapped to Invitation details
            .invitedBy(domain.getCreatedBy())
            .invitationDate(domain.getCreatedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: ProjectMemberResponseMapper.toResponse(domain,
     * ProjectMemberResponseMapper::attachUserEmail);
     */
    @SafeVarargs
    public static ProjectMemberResponse
    toResponse(ProjectMember domain,
               BiConsumer<ProjectMemberResponse, ProjectMember>... enrichers) {
        ProjectMemberResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<ProjectMemberResponse, ProjectMember> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachUserEmail(ProjectMemberResponse response,
                                       ProjectMember domain) {
        if (domain.getUser() != null) {
            response.setUserEmail(domain.getUser().getEmail());
        }
    }
}
