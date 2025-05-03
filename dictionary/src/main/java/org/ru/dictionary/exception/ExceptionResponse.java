package org.ru.dictionary.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private String errorType;
    private String description;
    private String message;
    private Integer code;

    public ExceptionResponse(String message, int status) {
        this.message = message;
        this.code = status;
    }
}
