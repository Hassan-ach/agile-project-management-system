package com.ensa.agile.infrastructure.persistence.jpa.user;

import com.ensa.agile.domain.user.entity.User;

public class UserJpaMapper {

    public static UserJpaEntity toJpaEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserJpaEntity(
            user.getId(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getPassword(), user.isEmailVerified(),
            user.isEnabled(), user.isLocked(), user.isCredentialsExpired(),
            user.getCreatedDate());
    }

    public static User toDomainEntity(UserJpaEntity user) {
        if (user == null) {
            return null;
        }
        return new User(user.getId(), user.getFirstName(), user.getLastName(),
                        user.getEmail(), user.getPassword(),
                        user.isEmailVerified(), user.isEnabled(),
                        user.isLocked(), user.isCredentialsExpired(),
                        user.getCreatedDate());
    }
}
