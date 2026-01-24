package com.ensa.agile.application.user.request;

import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.domain.global.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;
    private String password;

    // This constructor is for validation purposes
    public AuthenticationRequest(AuthenticationRequest req) {
        ValidationUtil.requireNonNull(req, "authentication request");

        this.email = req.email;
        this.password = req.password;

        validate();
    }

    public void validate() {
        ValidationUtil.requireValidEmail(email, "email");
        ValidationUtil.requireStrongPassword(password, "password");
    }
}
