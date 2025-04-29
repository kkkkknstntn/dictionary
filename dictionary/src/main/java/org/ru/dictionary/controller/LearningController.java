package org.ru.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.LearningType;
import org.ru.dictionary.service.LearningService;
import org.ru.dictionary.service.ProgressService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;
    private final ProgressService progressService;

    @GetMapping("/courses/{courseId}/levels/{levelId}")
    public LearningMaterialDTO getLearningMaterial(
            @PathVariable Long courseId,
            @PathVariable Long levelId,
            @RequestParam LearningType type,
            @AuthenticationPrincipal User user) {
        return learningService.generateLearningMaterial(
                courseId, levelId, user.getId(), type
        );
    }

    @PostMapping("/check-answer")
    public AnswerResultDTO checkAnswer(
            @RequestBody AnswerSubmissionDTO submission,
            @AuthenticationPrincipal User user) {
        return learningService.processAnswer(submission, user.getId());
    }
}
