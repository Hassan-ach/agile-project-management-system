package com.ensa.agile.presentation.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ensa.agile.application.epic.exception.EpicNotFoundException;
import com.ensa.agile.application.product.exception.ProductBackLogNotFoundException;
import com.ensa.agile.application.product.exception.ProjectMemberNotFoundException;
import com.ensa.agile.application.sprint.exception.SprintBackLogNotFoundException;
import com.ensa.agile.application.sprint.exception.SprintHistoryNotFoundException;
import com.ensa.agile.application.sprint.exception.SprintMemberNotFoundException;
import com.ensa.agile.application.story.exception.UserStoryHistoryNotFoundException;
import com.ensa.agile.application.story.exception.UserStoryNotFoundException;
import com.ensa.agile.application.task.exception.TaskHistoryNotFoundException;
import com.ensa.agile.application.task.exception.TaskNotFoundException;
import com.ensa.agile.application.user.exception.AuthenticationFailureException;
import com.ensa.agile.application.user.exception.EmailAlreadyUsedException;
import com.ensa.agile.application.user.exception.InvalidCredentialsException;
import com.ensa.agile.application.user.exception.UserAlreadyInvitedException;
import com.ensa.agile.application.user.exception.UserNotFoundException;
import com.ensa.agile.domain.global.exception.AlreadyExistsException;
import com.ensa.agile.domain.global.exception.ApplicationException;
import com.ensa.agile.domain.global.exception.BusinessRuleViolationException;
import com.ensa.agile.domain.global.exception.DataBasePersistenceException;
import com.ensa.agile.domain.global.exception.DataBaseTransactionException;
import com.ensa.agile.domain.global.exception.DomainException;
import com.ensa.agile.domain.global.exception.NotFoundException;
import com.ensa.agile.domain.global.exception.UnauthenticatedException;
import com.ensa.agile.domain.global.exception.ValidationException;
import com.ensa.agile.infrastructure.security.exception.JwtCreationException;
import com.ensa.agile.infrastructure.security.exception.JwtValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }

    // --- 400 Bad Request ---
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // --- 401 Unauthorized ---
    @ExceptionHandler({
        AuthenticationFailureException.class,
        InvalidCredentialsException.class,
        UnauthenticatedException.class,
        JwtValidationException.class
    })
    public ResponseEntity<Map<String, String>> handleAuthExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // --- 403 Forbidden ---
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Map<String, String>> handleForbidException(AccessDeniedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
        // return buildErrorResponse("Access Denied: You do not have permission to perform this action.", HttpStatus.FORBIDDEN);
    }

    // --- 404 Not Found ---
    @ExceptionHandler({
        NotFoundException.class,
        EpicNotFoundException.class,
        ProductBackLogNotFoundException.class,
        ProjectMemberNotFoundException.class,
        SprintBackLogNotFoundException.class,
        SprintHistoryNotFoundException.class,
        SprintMemberNotFoundException.class,
        UserStoryHistoryNotFoundException.class,
        UserStoryNotFoundException.class,
        TaskHistoryNotFoundException.class,
        TaskNotFoundException.class,
        UserNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // --- 409 Conflict ---
    @ExceptionHandler({
        AlreadyExistsException.class,
        EmailAlreadyUsedException.class,
        UserAlreadyInvitedException.class
    })
    public ResponseEntity<Map<String, String>> handleConflictExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // --- 422 Unprocessable Content (Business Logic) ---
    @ExceptionHandler({
        BusinessRuleViolationException.class,
        DomainException.class
    })
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(Exception ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    // --- 500 Internal Server Error ---
    @ExceptionHandler({
        DataBasePersistenceException.class,
        DataBaseTransactionException.class,
        ApplicationException.class,
        JwtCreationException.class
    })
    public ResponseEntity<Map<String, String>> handleKnownInternalExceptions(Exception ex) {
        // Log the full stack trace internally
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        // return buildErrorResponse("An internal error occurred. Please contact the administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Catch-All (Runtime) ---
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleUncaughtRuntimeException(RuntimeException ex) {
        // SECURITY: Never return ex.getMessage() to the client for unknown errors.
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        // return buildErrorResponse("A critical system error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
