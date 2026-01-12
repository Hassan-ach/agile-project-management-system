package com.ensa.agile.domain.global.exception;

public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) { super(message); }

    public BusinessRuleViolationException() {
        super("Business rule violation occurred.");
    }
}
