package com.ensa.agile.infrastructure.persistence.jpa.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ensa.agile.application.user.security.IPasswordEncoder;
import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.infrastructure.security.utils.PasswordEncoderImpl;
import com.ensa.agile.testfactory.TestUserFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaUserRepositoryTest {
    @Autowired
    private  JpaUserRepository jpaUserRepository;

    private  IPasswordEncoder passwordEncoder;

    @BeforeEach
    void init(){
        passwordEncoder = new PasswordEncoderImpl(new BCryptPasswordEncoder());
    }

    @Test
    void save_shouldSaveUserSuccessfully() {
        // Given
        String encodedPassword = passwordEncoder.encode("password");
        User user = TestUserFactory.validUserWithPassword("test@gmail.com",encodedPassword);

        // When
        User savedUser = UserJpaMapper.toDomainEntity(
            jpaUserRepository.save(UserJpaMapper.toJpaEntity(user)));

        assert savedUser != null;
        assert savedUser.getId() != null;
        assert savedUser.getEmail().equals(user.getEmail());
    }

    @Test
    void existsByEmailIgnoreCase_shouldReturnTrueIfUserExists() {
        String email = "test@gmail.com";
        String encodedPassword = passwordEncoder.encode("password");
        User user = TestUserFactory.validUserWithPassword(email,encodedPassword);

        jpaUserRepository.save(UserJpaMapper.toJpaEntity(user));

        boolean exists = jpaUserRepository.existsByEmailIgnoreCase(email.toUpperCase());

        assert exists;
    }

}
