package com.ensa.agile.application.product.mapper;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import com.ensa.agile.application.epic.mapper.EpicResponseMapper;
import com.ensa.agile.application.product.response.ProductBackLogResponse;
import com.ensa.agile.application.story.mapper.UserStoryResponseMapper;
import com.ensa.agile.domain.product.entity.ProductBackLog;

public class ProductBackLogResponseMapper {

    // ========================================================================
    // DOMAIN -> RESPONSE
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static ProductBackLogResponse toResponse(ProductBackLog domain) {
        if (domain == null)
            return null;

        return ProductBackLogResponse.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            // Audit Metadata
            .createdBy(domain.getCreatedBy())
            .createdDate(domain.getCreatedDate())
            .lastModifiedBy(domain.getLastModifiedBy())
            .lastModifiedDate(domain.getLastModifiedDate())
            .epics(new ArrayList<>())
            .userStories(new ArrayList<>())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     * Usage: ProductBackLogResponseMapper.toResponse(domain,
     * ProductBackLogResponseMapper::attachEpics);
     */
    @SafeVarargs
    public static ProductBackLogResponse toResponse(
        ProductBackLog domain,
        BiConsumer<ProductBackLogResponse, ProductBackLog>... enrichers) {
        ProductBackLogResponse response = toResponse(domain);
        if (response != null && enrichers != null) {
            for (BiConsumer<ProductBackLogResponse, ProductBackLog> enricher :
                 enrichers) {
                enricher.accept(response, domain);
            }
        }
        return response;
    }

    // 3. Attachers
    public static void attachMembers(ProductBackLogResponse response,
                                     ProductBackLog domain) {
        if (domain.getProjectMembers() != null) {
            response.setProjectMembers(
                domain.getProjectMembers()
                    .stream()
                    .map(ProjectMemberResponseMapper::toResponse)
                    .toList());
        }
    }
    public static void attachEpics(ProductBackLogResponse response,
                                   ProductBackLog domain) {
        if (domain.getEpics() != null) {
            response.setEpics(domain.getEpics()
                                  .stream()
                                  .map(EpicResponseMapper::toResponse)
                                  .toList());
        }
    }

    public static void attachUserStories(ProductBackLogResponse response,
                                         ProductBackLog domain) {
        if (domain.getUserStories() != null) {
            response.setUserStories(
                domain.getUserStories()
                    .stream()
                    .map(UserStoryResponseMapper::toResponse)
                    .toList());
        }
    }

    public static void attachEpicsWithStories(ProductBackLogResponse response,
                                              ProductBackLog domain) {
        if (domain.getEpics() != null) {
            response.setEpics(
                domain.getEpics()
                    .stream()
                    .map(epic
                         -> EpicResponseMapper.toResponse(
                             epic, EpicResponseMapper::attachUserStories))
                    .toList());
        }
    }
}
