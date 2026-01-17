package com.ensa.agile.testfactory;

import java.time.LocalDate;
import java.util.UUID;

import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.infrastructure.persistence.jpa.user.UserJpaEntity;

public final class TestUserFactory {

    public static User validUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return validUserWithEmail("test" + uniqueId + "@gmail.com");
    }

    public static User validUserWithEmail(String email) {
        return validUserWithPassword(email, "test1234");
    }

    public static User validUserWithPassword(String email, String password) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
            .firstName("First" + uniqueId)
            .lastName("Last" + uniqueId)
            .email(email)
            .password(password)
            .createdDate(LocalDate.now())
            .build();
    }

    public static UserJpaEntity validJpaUserWithEmailAndPassword(String email, String password){
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return UserJpaEntity.builder()
            .firstName("First" + uniqueId)
            .lastName("Last" + uniqueId)
            .email(email)
            .password(password)
            .createdDate(LocalDate.now())
            .build();
    }
    public static UserJpaEntity validJpaUser(){
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return validJpaUserWithEmail("test" + uniqueId + "@gmail.com");
    }

    public static UserJpaEntity validJpaUserWithEmail(String email){
        return validJpaUserWithEmailAndPassword(email, "test1234");
    }
}
