package com.ensa.agile.domain.global.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean patternMatches(String emailAddress,
                                         String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return patternMatches(email, EMAIL_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        // At least one digit, one lower case, one upper case, one special
        // character, no whitespace, length between 8 and 20
        // String PASSWORD_PATTERN =
        //     "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])"
        //     + "(?=\\S+$).{8,20}$";
        // return patternMatches(password, PASSWORD_PATTERN);
        return true;
    }

    // public static boolean isValidUUID(UUID uuid) {
    //     String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-"
    //                           + "[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    //     return patternMatches(uuid.toString(), UUID_PATTERN);
    // }
}
