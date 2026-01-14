package com.ensa.agile.domain.user.entity;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean emailVerified;
    private boolean enabled;
    private boolean locked;
    private boolean credentialsExpired;
    private LocalDate createdDate;

    public User(String id, String firstName, String lastName, String email,
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
        this.createdDate = createdDate;

        validate();
    }

    public void validate() {
        if (this.firstName == null || this.firstName.isEmpty()) {
            throw new ValidationException("First name cannot be null or empty");
        }
        if (this.lastName == null || this.lastName.isEmpty()) {
            throw new ValidationException("Last name cannot be null or empty");
        }
        if (this.email == null || this.email.isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        if (this.password == null || this.password.isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }

        if (!ValidationUtil.isValidPassword(this.password)) {
            throw new ValidationException(
                "Password does not meet complexity requirements");
        }

        if (!ValidationUtil.isValidEmail(this.email)) {
            throw new ValidationException("Email format is invalid");
        }
    }

    public void updateMetadata(String firstName, String lastName,
                               String password) {
        if (firstName != null) {
            this.firstName = firstName;
        }
        if (lastName != null) {
            this.lastName = lastName;
        }
        if (password != null) {
            this.password = password;
        }

        validate();
    }

    public void verifyEmail() { this.emailVerified = true; }
    public void lockAccount() { this.locked = true; }
    public void unlockAccount() { this.locked = false; }
    public void expireCredentials() { this.credentialsExpired = true; }
    public void renewCredentials() { this.credentialsExpired = false; }

    public List<String> getAuthorities() { return new ArrayList<String>(); }
}
