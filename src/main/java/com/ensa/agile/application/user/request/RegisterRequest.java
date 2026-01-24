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

        ValidationUtil.requireMinLength(firstName, "user first name", 3);

        ValidationUtil.requireMinLength(lastName, "user last name", 3);
        ValidationUtil.requireValidEmail(email, "user email");

        ValidationUtil.requireStrongPassword(password, "user password");

        ValidationUtil.require(passwordConfirm, "password Confirm", (p) -> {
            return p != null && p.equals(password);
        }, "Password confirmation does not match the password");
    }
}
