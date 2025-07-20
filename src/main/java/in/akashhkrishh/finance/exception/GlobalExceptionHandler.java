package in.akashhkrishh.finance.exception;

import in.akashhkrishh.finance.dto.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND,"User not found", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponse<Object>> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Illegal Argument", ex.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Transaction not found", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT,"User already exists", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,"Invalid JSON format", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,"JSON Validation error", ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<GlobalResponse<Object>> handleNoSuchElementException(NoSuchElementException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<GlobalResponse<Object>> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "User not authenticated", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GlobalResponse<Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials", ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage());
    }

    private ResponseEntity<GlobalResponse<Object>> buildResponse(HttpStatus status, String msg, String err) {
        return ResponseEntity.status(status)
                .body(new GlobalResponse<>(null, msg, err, false));
    }
}
