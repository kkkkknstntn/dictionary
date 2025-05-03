package org.ru.dictionary.exception;

import lombok.Getter;
import org.ru.dictionary.enums.BusinessErrorCodes;

@Getter
public class ApiException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public ApiException(BusinessErrorCodes errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public ApiException(BusinessErrorCodes errorCode, String additionalMessage) {
        super(errorCode.getDescription() + ": " + additionalMessage);
        this.errorCode = errorCode;
    }
}