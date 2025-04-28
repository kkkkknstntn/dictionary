package org.ru.dictionary.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Setter
@Getter
public class ExceptionResponse {
    private Integer businessErrorCode;
    private String businessErrorDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Integer businessErrorCode, String businessErrorDescription, String error) {
        this.businessErrorCode = businessErrorCode;
        this.businessErrorDescription = businessErrorDescription;
        this.error = error;
    }

    public ExceptionResponse(Set<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ExceptionResponse(String error) {
        this.error = error;
    }
}