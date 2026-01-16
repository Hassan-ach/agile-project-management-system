package com.ensa.agile.infrastructure.security.service;

import com.ensa.agile.application.global.service.ICurrentUserService;
import com.ensa.agile.domain.global.exception.UnauthenticatedException;
import com.ensa.agile.domain.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements ICurrentUserService {
    public User getCurrentUser() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedException(
                "No active security context found.");
        }
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User)) {
            throw new UnauthenticatedException(
                "No active security context found.");
        }

        return (User)principal;
    }
}
