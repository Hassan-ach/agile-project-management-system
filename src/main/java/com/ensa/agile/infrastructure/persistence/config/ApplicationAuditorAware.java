package com.ensa.agile.infrastructure.persistence.config;

import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        final Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        final UserJpaEntity user =
            UserJpaMapper.toJpaEntity((User)authentication.getPrincipal());
        return Optional.ofNullable(user.getId());
    }
}
