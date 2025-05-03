package org.ru.dictionary.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(InvalidTokenException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.INVALID_TOKEN.getCode(),
                                BusinessErrorCodes.INVALID_TOKEN.getDescription(),
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionResponse> handleFileUploadException(FileUploadException exp) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        new ExceptionResponse(
                               null,
                                null,
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exp) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.USER_NOT_FOUND.getCode(),
                                BusinessErrorCodes.USER_NOT_FOUND.getDescription(),
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExistsException exp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.USER_ALREADY_EXISTS.getCode(),
                                BusinessErrorCodes.USER_ALREADY_EXISTS.getDescription(),
                                "User with this email / username already exists"
                        )
                );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.ACCOUNT_LOCKED.getCode(),
                                BusinessErrorCodes.ACCOUNT_LOCKED.getDescription(),
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.ACCOUNT_DISABLED.getCode(),
                                BusinessErrorCodes.ACCOUNT_DISABLED.getDescription(),
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ExceptionResponse(
                                BusinessErrorCodes.BAD_CREDENTIALS.getCode(),
                                BusinessErrorCodes.BAD_CREDENTIALS.getDescription(),
                                BusinessErrorCodes.BAD_CREDENTIALS.getDescription()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getFieldErrors().forEach(fieldError -> {
            if (fieldError.getDefaultMessage() != null) {
                errors.add(fieldError.getDefaultMessage());
            }
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ExceptionResponse(
                                null,
                                "Very bad(",
                                exp.getMessage()
                        )
                );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(null, null, "Category not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(null, null, "Product with article not found"));
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}