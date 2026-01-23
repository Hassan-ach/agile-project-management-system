package com.ensa.agile.infrastructure.persistence.jpa.user;

import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaMapper.toJpaEntity(user);
        UserJpaEntity saved = jpaUserRepository.save(entity);
        return UserJpaMapper.toDomain(saved);
    }

    @Override
    public User findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
            .map(UserJpaMapper::toDomain)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public List<User> findAll() {
        List<UserJpaEntity> entities = jpaUserRepository.findAll();
        return entities.stream().map(UserJpaMapper::toDomain).toList();
    }

    @Override
    public User findById(UUID id) {
        return jpaUserRepository.findById(id)
            .map(UserJpaMapper::toDomain)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserRepository.existsById(id);
    }
}
