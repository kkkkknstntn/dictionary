package org.ru.dictionary.dto;

import lombok.Data;
import org.ru.dictionary.enums.LearningType;

@Data
public class AnswerSubmissionDTO {
    private Long wordId;
    private String answer;
    private LearningType type;
}