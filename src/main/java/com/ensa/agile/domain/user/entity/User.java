package com.ensa.agile.domain.user.entity;

import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class User {

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

    public User(UUID id, String firstName, String lastName, String email,
                String password, Boolean emailVerified, Boolean enabled,
                Boolean locked, Boolean credentialsExpired,
                LocalDate createdDate) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified == null ? false : emailVerified;
        this.enabled = enabled == null ? true : enabled;
        this.locked = locked == null ? false : locked;
        this.credentialsExpired =
            credentialsExpired == null ? false : credentialsExpired;
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;

        validate();
    }

    public void validate() {
        ValidationUtil.requireNonBlank(this.firstName, "user first name");
        ValidationUtil.requireNonBlank(this.lastName, "user last name");
        ValidationUtil.requireStrongPassword(this.password, "user password");

        ValidationUtil.requireValidEmail(this.email, "user email");
    }

    public void updateMetadata(String firstName, String lastName,
                               String password) {

        this.firstName = ValidationUtil.update(this.firstName, firstName,
                                               ValidationUtil::requireNonBlank,
                                               "user first name");

        this.lastName = ValidationUtil.update(this.lastName, lastName,
                                              ValidationUtil::requireNonBlank,
                                              "user last name");

        this.password = ValidationUtil.update(
            this.password, password, ValidationUtil::requireStrongPassword,
            "user password");
    }

    public void verifyEmail() { this.emailVerified = true; }
    public void lockAccount() { this.locked = true; }
    public void unlockAccount() { this.locked = false; }
    public void expireCredentials() { this.credentialsExpired = true; }
    public void renewCredentials() { this.credentialsExpired = false; }

    public List<String> getAuthorities() { return new ArrayList<String>(); }
}
