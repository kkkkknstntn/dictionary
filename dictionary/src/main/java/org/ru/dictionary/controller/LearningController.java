package org.ru.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.enums.LearningType;
import org.ru.dictionary.service.LearningServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class LearningController {

    private final LearningServiceImpl learningService;

    @GetMapping("/{levelId}")
    public LearningMaterialDTO getLearningMaterial(
            @PathVariable Long levelId,
            @RequestParam LearningType type,
            @AuthenticationPrincipal UserDetails userDetails) {
        return learningService.generateLearningMaterial(levelId, userDetails, type
        );
    }

    @PostMapping("/check-answer")
    public AnswerResultDTO checkAnswer(
            @RequestBody AnswerSubmissionDTO submission,
            @AuthenticationPrincipal UserDetails userDetails) {
        return learningService.processAnswer(submission, userDetails);
    }
}
