package com.ensa.agile.testfactory;

import com.ensa.agile.domain.user.entity.User;
import java.util.UUID;

public final class TestUserFactory {

    public static User validUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return User.builder()
            .firstName("First" + uniqueId)
            .lastName("Last" + uniqueId)
            .email("test" + uniqueId + "@gmail.com")
            .password("Test@1234")
            .build();
    }

    public static User validUserWithEmail(String email) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
            .firstName("First" + uniqueId)
            .lastName("Last" + uniqueId)
            .email(email)
            .password("Test@1234")
            .build();
    }
}
