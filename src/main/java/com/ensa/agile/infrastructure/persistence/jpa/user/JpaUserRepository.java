package com.ensa.agile.infrastructure.persistence.jpa.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {

	Optional<UserJpaEntity> findByEmail(String email);

	boolean existsByEmailIgnoreCase(String email);

}
