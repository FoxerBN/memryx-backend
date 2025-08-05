package sk.foxer.flashcard.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.api.response.ErrorResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return buildError(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return buildError(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildError(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(EmptyBodyException.class)
    public ResponseEntity<ErrorResponse> handleEmptyBody(EmptyBodyException ex) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DangerousInputException.class)
    public ResponseEntity<ErrorResponse> handleDangerous(DangerousInputException ex) {
        return buildError(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return buildError(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildError("Vnútorná chyba servera", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        return buildError("Request body je prázdny alebo má zlý formát.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleSpringAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return buildError("Nemáte oprávnenie na túto akciu.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleSpringAuth(org.springframework.security.core.AuthenticationException ex) {
        return buildError("Chyba autentifikácie: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildError(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildError(String message, HttpStatus status) {
        ErrorResponse err = new ErrorResponse(LocalDateTime.now(), status.value(), message);
        return new ResponseEntity<>(err, status);
    }
}
