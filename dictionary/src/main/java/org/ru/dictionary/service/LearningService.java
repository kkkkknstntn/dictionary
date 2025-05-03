package org.ru.dictionary.service;

import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.enums.LearningType;
import org.springframework.security.core.userdetails.UserDetails;

public interface LearningService {
    AnswerResultDTO processAnswer(AnswerSubmissionDTO submission, UserDetails userDetails);
    LearningMaterialDTO generateLearningMaterial(Long levelId, UserDetails userDetails, LearningType type);
}
