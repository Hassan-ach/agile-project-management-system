package com.ensa.agile.infrastructure.persistence.jpa.user;

import com.ensa.agile.domain.user.entity.User;
import java.util.function.BiConsumer;

public class UserJpaMapper {

    // ========================================================================
    // A. ENTITY -> DOMAIN (READ)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static User toDomain(UserJpaEntity entity) {
        if (entity == null)
            return null;

        // Switched to Builder pattern to match the requested architecture
        return User.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .emailVerified(entity.getEmailVerified() == null
                               ? false
                               : entity.getEmailVerified())
            .enabled(entity.getEnabled() == null ? false : entity.getEnabled())
            .locked(entity.getLocked() == null ? true : entity.getLocked())
            .credentialsExpired(entity.getCredentialsExpired() == null
                                    ? true
                                    : entity.getCredentialsExpired())
            .createdDate(entity.getCreatedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static User toDomain(UserJpaEntity entity,
                                BiConsumer<User, UserJpaEntity>... enrichers) {
        User domain = toDomain(entity);
        if (domain != null && enrichers != null) {
            for (BiConsumer<User, UserJpaEntity> enricher : enrichers) {
                enricher.accept(domain, entity);
            }
        }
        return domain;
    }

    // 3. Attachers
    // Add methods here if User has lazy relationships (e.g. Roles, Tasks) to
    // load.

    // ========================================================================
    // B. DOMAIN -> ENTITY (WRITE)
    // ========================================================================

    /**
     * 1. Base Mapper: Maps strict metadata only.
     */
    public static UserJpaEntity toJpaEntity(User domain) {
        if (domain == null)
            return null;

        return UserJpaEntity.builder()
            .id(domain.getId())
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .email(domain.getEmail())
            .password(domain.getPassword())
            .emailVerified(domain.isEmailVerified())
            .enabled(domain.isEnabled())
            .locked(domain.isLocked())
            .credentialsExpired(domain.isCredentialsExpired())
            .createdDate(domain.getCreatedDate())
            .build();
    }

    /**
     * 2. Orchestrator: Map metadata + specific relationships.
     */
    @SafeVarargs
    public static UserJpaEntity
    toJpaEntity(User domain, BiConsumer<UserJpaEntity, User>... enrichers) {
        UserJpaEntity entity = toJpaEntity(domain);
        if (entity != null && enrichers != null) {
            for (BiConsumer<UserJpaEntity, User> enricher : enrichers) {
                enricher.accept(entity, domain);
            }
        }
        return entity;
    }
}
