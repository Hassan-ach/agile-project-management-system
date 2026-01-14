package com.ensa.agile.presentation.advice;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.user.exception.AuthenticationFailureException;
import com.ensa.agile.application.user.exception.EmailAlreadyUsedException;
import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.exception.UserAlreadyInvitedException;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.global.exception.AlreadyExistsException;
import com.ensa.agile.domain.global.exception.ApplicationException;
import com.ensa.agile.domain.global.exception.BusinessRuleViolationException;
import com.ensa.agile.domain.global.exception.DataBaseTransactionException;
import com.ensa.agile.domain.global.exception.DomainException;
import com.ensa.agile.domain.global.exception.ForbidException;
import com.ensa.agile.domain.global.exception.NotFoundException;
import com.ensa.agile.domain.global.exception.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>>

    buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Map<String, String>>
        handleBadRequestExceptions(ValidationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbidException.class)
    public ResponseEntity<Map<String, String>>
    handleForbidException(ForbidException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NotFoundException.class, DomainException.class,
                       UserNotFoundException.class, EpicNotFoundException.class,
                       ProductBackLogNotFoundException.class})
    public ResponseEntity<Map<String, String>>
        handleNotFoundExceptions(NotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AlreadyExistsException.class,
                       EmailAlreadyUsedException.class,
                       UserAlreadyInvitedException.class})
    public ResponseEntity<Map<String, String>>
        handleConflictExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<Map<String, String>>
    handleIllegalStateException(BusinessRuleViolationException ex) {
        return buildErrorResponse(ex.getMessage(),
                                  HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler({AuthenticationFailureException.class,
                       InvalidCredentialsException.class})
    public ResponseEntity<Map<String, String>>
        HandleJwtExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({DataBaseTransactionException.class,
                       ApplicationException.class})
    public ResponseEntity<Map<String, String>>
        handleDataBaseException(Exception ex) {
        return buildErrorResponse(ex.getMessage(),
                                  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>>
    handleUncaughtRuntimeException(RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "A critical system error occurred.");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
