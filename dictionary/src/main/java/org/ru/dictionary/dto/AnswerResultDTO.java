package org.ru.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerResultDTO {
    private boolean isCorrect;
    private int newProgress;
}