package com.ensa.agile.domain.global.utils;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Utility class for domain entity validation.
 * Provides fluent API for common validation scenarios.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern URL_PATTERN =
        Pattern.compile("^https?://[\\w\\-.]+(:\\d+)?(/.*)?$");

    private ValidationUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    // ==================== NULL CHECKS ====================

    /**
     * Validates that value is not null.
     * @throws ValidationException if value is null
     */
    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
        return value;
    }

    /**
     * Validates that all values are not null.
     */
    public static void requireAllNonNull(Object... values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                throw new ValidationException("Field at index " + i +
                                              " cannot be null");
            }
        }
    }

    // ==================== STRING VALIDATIONS ====================

    /**
     * Validates that string is not null or empty.
     */
    public static String requireNonEmpty(String value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        return value;
    }

    /**
     * Validates that string is not null, empty, or whitespace.
     */
    public static String requireNonBlank(String value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new ValidationException(fieldName + " cannot be blank");
        }
        return value;
    }

    /**
     * Validates string length is within bounds.
     */
    public static String requireLength(String value, String fieldName, int min,
                                       int max) {
        requireNonNull(value, fieldName);
        int length = value.length();
        if (length < min || length > max) {
            throw new ValidationException(String.format(
                "%s must be between %d and %d characters (got %d)", fieldName,
                min, max, length));
        }
        return value;
    }

    /**
     * Validates string has minimum length.
     */
    public static String requireMinLength(String value, String fieldName,
                                          int minLength) {
        requireNonNull(value, fieldName);
        if (value.length() < minLength) {
            throw new ValidationException(
                String.format("%s must be at least %d characters (got %d)",
                              fieldName, minLength, value.length()));
        }
        return value;
    }

    /**
     * Validates password strength.
     */
    public static String requireStrongPassword(String password,
                                               String fieldName) {
        requireNonNull(password, fieldName);
        // not implemented yet
        return password;
    }

    /**
     * Validates string has maximum length.
     */
    public static String requireMaxLength(String value, String fieldName,
                                          int maxLength) {
        requireNonNull(value, fieldName);
        if (value.length() > maxLength) {
            throw new ValidationException(
                String.format("%s must not exceed %d characters (got %d)",
                              fieldName, maxLength, value.length()));
        }
        return value;
    }

    /**
     * Validates string matches pattern.
     */
    public static String requirePattern(String value, String fieldName,
                                        Pattern pattern,
                                        String patternDescription) {
        requireNonNull(value, fieldName);
        if (!pattern.matcher(value).matches()) {
            throw new ValidationException(String.format(
                "%s must match pattern: %s", fieldName, patternDescription));
        }
        return value;
    }

    /**
     * Validates email format.
     */
    public static String requireValidEmail(String email, String fieldName) {
        requireNonBlank(email, fieldName);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException(fieldName +
                                          " must be a valid email address");
        }
        return email;
    }

    /**
     * Validates URL format.
     */
    public static String requireValidUrl(String url, String fieldName) {
        requireNonBlank(url, fieldName);
        if (!URL_PATTERN.matcher(url).matches()) {
            throw new ValidationException(fieldName + " must be a valid URL");
        }
        return url;
    }

    // ==================== NUMERIC VALIDATIONS ====================

    /**
     * Validates number is positive (> 0).
     */
    public static <T extends Number & Comparable<T>>
        T requirePositive(T value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() <= 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
        return value;
    }

    /**
     * Validates number is non-negative (>= 0).
     */
    public static <T extends Number & Comparable<T>>
        T requireNonNegative(T value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() < 0) {
            throw new ValidationException(fieldName + (" must be "
                                                       + "non-negative"));
        }
        return value;
    }

    /**
     * Validates number is within range (inclusive).
     */
    public static <T extends Number & Comparable<T>>
        T requireRange(T value, String fieldName, T min, T max) {
        requireNonNull(value, fieldName);
        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();

        if (val < minVal || val > maxVal) {
            throw new ValidationException(
                String.format("%s must be between %s and %s (got %s)",
                              fieldName, min, max, value));
        }
        return value;
    }

    /**
     * Validates number is greater than minimum.
     */
    public static <T extends Number & Comparable<T>>
        T requireMin(T value, String fieldName, T min) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() < min.doubleValue()) {
            throw new ValidationException(String.format(
                "%s must be at least %s (got %s)", fieldName, min, value));
        }
        return value;
    }

    /**
     * Validates number is less than maximum.
     */
    public static <T extends Number & Comparable<T>>
        T requireMax(T value, String fieldName, T max) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() > max.doubleValue()) {
            throw new ValidationException(String.format(
                "%s must not exceed %s (got %s)", fieldName, max, value));
        }
        return value;
    }

    // ==================== DATE/TIME VALIDATIONS ====================

    /**
     * Validates date is in the future.
     */
    public static LocalDate requireFutureDate(LocalDate date,
                                              String fieldName) {
        requireNonNull(date, fieldName);
        if (!date.isAfter(LocalDate.now())) {
            throw new ValidationException(fieldName + (" must be a future "
                                                       + "date"));
        }
        return date;
    }

    /**
     * Validates date is in the past.
     */
    public static LocalDate requirePastDate(LocalDate date, String fieldName) {
        requireNonNull(date, fieldName);
        if (!date.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " must be a past date");
        }
        return date;
    }

    /**
     * Validates date is after another date.
     */
    public static LocalDate requireAfter(LocalDate date, String fieldName,
                                         LocalDate afterDate,
                                         String afterFieldName) {
        requireNonNull(date, fieldName);
        requireNonNull(afterDate, afterFieldName);
        if (!date.isAfter(afterDate)) {
            throw new ValidationException(String.format(
                "%s must be after %s", fieldName, afterFieldName));
        }
        return date;
    }

    /**
     * Validates date is before another date.
     */
    public static LocalDate requireBefore(LocalDate date, String fieldName,
                                          LocalDate beforeDate,
                                          String beforeFieldName) {
        requireNonNull(date, fieldName);
        requireNonNull(beforeDate, beforeFieldName);
        if (!date.isBefore(beforeDate)) {
            throw new ValidationException(String.format(
                "%s must be before %s", fieldName, beforeFieldName));
        }
        return date;
    }

    /**
     * Validates datetime is in the future.
     */
    public static LocalDateTime requireFutureDateTime(LocalDateTime dateTime,
                                                      String fieldName) {
        requireNonNull(dateTime, fieldName);
        if (!dateTime.isAfter(LocalDateTime.now())) {
            throw new ValidationException(fieldName +
                                          " must be a future date/time");
        }
        return dateTime;
    }

    /**
     * Validates datetime is in the past.
     */
    public static LocalDateTime requirePastDateTime(LocalDateTime dateTime,
                                                    String fieldName) {
        requireNonNull(dateTime, fieldName);
        if (!dateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException(fieldName +
                                          " must be a past date/time");
        }
        return dateTime;
    }

    // ==================== ENUM VALIDATIONS ====================

    /**
     * Validates enum is one of allowed values.
     */
    @SafeVarargs
    public static <E extends Enum<E>> E requireOneOf(E value, String fieldName,
                                                     E... allowedValues) {
        requireNonNull(value, fieldName);
        for (E allowed : allowedValues) {
            if (value == allowed) {
                return value;
            }
        }
        throw new ValidationException(
            String.format("%s must be one of %s (got %s)", fieldName,
                          java.util.Arrays.toString(allowedValues), value));
    }

    // ==================== UUID VALIDATIONS ====================

    /**
     * Validates UUID is not null.
     */
    public static UUID requireValidId(UUID id, String fieldName) {
        return requireNonNull(id, fieldName);
    }

    // ==================== CUSTOM VALIDATIONS ====================

    /**
     * Validates value meets custom condition.
     */
    public static <T> T require(T value, String fieldName,
                                Predicate<T> condition, String errorMessage) {
        requireNonNull(value, fieldName);
        if (!condition.test(value)) {
            throw new ValidationException(fieldName + " " + errorMessage);
        }
        return value;
    }

    /**
     * Validates boolean condition is true.
     */
    public static void requireTrue(boolean condition, String errorMessage) {
        if (!condition) {
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * Validates boolean condition is false.
     */
    public static void requireFalse(boolean condition, String errorMessage) {
        if (condition) {
            throw new ValidationException(errorMessage);
        }
    }

    // ==================== BUSINESS RULE VALIDATIONS ====================

    /**
     * Validates state transition is allowed.
     */
    public static <T extends Enum<T>>
        T requireStateTransition(T currentState, T newState, String entityName,
                                 Predicate<T> isValidTransition) {

        requireNonNull(currentState, "Current state");
        requireNonNull(newState, "New state");

        if (!isValidTransition.test(newState)) {
            throw new ValidationException(
                String.format("Cannot transition %s from %s to %s", entityName,
                              currentState, newState));
        }
        return newState;
    }

    // ==================== OPTIONAL VALIDATIONS ====================

    public static <T> T update(T past, T next, BiConsumer<T, String> validator,
                               String fieldName) {
        if (next != null) {
            // try {
            //     validator.accept(past, fieldName);
            // } catch (Exception e) {
            //     return past;
            // }
            validator.accept(past, fieldName);
            return next;
        }
        return past;
    }
}
