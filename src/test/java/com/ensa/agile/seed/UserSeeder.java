package com.ensa.agile.seed;

import java.util.stream.IntStream;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ensa.agile.infrastructure.persistence.jpa.user.JpaUserRepository;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;
import com.ensa.agile.testfactory.TestUserFactory; 

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void seed() {
        if (userRepository.count() > 0) return; 

        
        IntStream.range(0, 5).forEach(i -> {
             // Logic to create UserJpaEntity directly or via mapped factory
             UserJpaEntity user = TestUserFactory.validJpaUserWithEmailAndPassword("Test" + i + "@gmail.com",passwordEncoder.encode("password"));
            userRepository.save(user);
        });
    }
}
