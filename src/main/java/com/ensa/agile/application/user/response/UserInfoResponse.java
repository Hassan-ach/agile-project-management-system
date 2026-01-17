package com.ensa.agile.application.user.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean emailVerified;
    private boolean enabled;
    private boolean locked;
    private boolean credentialsExpired;
    private LocalDate createdDate;
	
}
