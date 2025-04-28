package org.ru.dictionary.enums;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Incorrect password"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.UNAUTHORIZED, "Login and/or password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),
    USER_ALREADY_EXISTS(305, HttpStatus.BAD_REQUEST, "User already exists"),
    INVALID_TOKEN(306, HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(307, HttpStatus.UNAUTHORIZED, "Token has expired"),
    USER_NOT_FOUND(308, HttpStatus.NOT_FOUND, "User not found");

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}