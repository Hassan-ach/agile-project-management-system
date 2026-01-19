package com.ensa.agile.application.user.mapper;

import com.ensa.agile.application.user.response.UserInfoResponse;
import com.ensa.agile.domain.user.entity.User;

public class UserInfoResponseMapper {
    public static UserInfoResponse toResponse(User user) {
        if(user == null) {
            return new UserInfoResponse();
        }
        return UserInfoResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .password(user.getPassword())
            .emailVerified(user.isEmailVerified())
            .enabled(user.isEnabled())
            .locked(user.isLocked())
            .credentialsExpired(user.isCredentialsExpired())
            .createdDate(user.getCreatedDate())
            .build();
    }
}
