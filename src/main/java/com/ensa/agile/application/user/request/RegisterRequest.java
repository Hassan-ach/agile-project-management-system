package com.ensa.agile.application.user.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;

    // This constructor is for validation purposes
    public RegisterRequest(RegisterRequest req) {
        this.firstName = req.firstName;
        this.lastName = req.lastName;
        this.email = req.email;
        this.password = req.password;
        this.passwordConfirm = req.passwordConfirm;
        validate();
    }

    public RegisterRequest(String firstName, String lastName, String email,
                           String password, String passwordConfirm) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        validate();
    }

    public void validate() throws ValidationException {

        if (firstName == null || firstName.trim().isBlank() ||
            firstName.length() < 3) {
            throw new ValidationException(
                "First name must be at least 3 characters long and cannot be "
                + "blank");
        }
        if (lastName == null || lastName.trim().isBlank() ||
            lastName.length() < 3) {
            throw new ValidationException(
                "Last name must be at least 3 characters long and cannot be "
                + "blank");
        }
        if (email == null || email.trim().isBlank() ||
            !ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("Email must be valid and cannot be "
                                          + "blank");
        }
        if (password == null || password.length() < 8) {
            throw new ValidationException(
                "Password must be at least 8 characters long if provided");
        }
        if (passwordConfirm == null || !passwordConfirm.equals(password)) {
            throw new ValidationException(
                "Password confirmation does not match "
                + "the password");
        }
    }
}
