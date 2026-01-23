package com.ensa.agile.domain.user.repository;

import java.util.UUID;

import com.ensa.agile.domain.global.repository.BaseDomainRepository;
import com.ensa.agile.domain.user.entity.User;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface UserRepository extends BaseDomainRepository<User, UUID> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
