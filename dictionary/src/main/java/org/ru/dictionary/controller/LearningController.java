package org.ru.dictionary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.enums.LearningType;
import org.ru.dictionary.service.LearningService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
@Tag(name = "Learning", description = "API для процесса обучения и проверки знаний")
public class LearningController {

    private final LearningService learningService;

    @Operation(
            summary = "Получить учебный материал",
            description = "Генерирует задание для обучения на основе прогресса пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учебный материал успешно сгенерирован"),
            @ApiResponse(responseCode = "400", description = "Некорректный тип задания"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Уровень/пользователь/слова не найдены")
    })
    @GetMapping("/{levelId}")
    public LearningMaterialDTO getLearningMaterial(
            @Parameter(description = "ID уровня", example = "123")
            @PathVariable Long levelId,
            @Parameter(description = "Тип задания",
                    required = true,
                    schema = @Schema(implementation = LearningType.class))
            @RequestParam LearningType type,
            @AuthenticationPrincipal UserDetails userDetails) {
        return learningService.generateLearningMaterial(levelId, userDetails, type
        );
    }

    @Operation(
            summary = "Проверить ответ",
            description = "Проверяет правильность ответа и обновляет прогресс"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат проверки ответа"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные ответа"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Слово/пользователь не найдены")
    })
    @PostMapping("/check-answer")
    public AnswerResultDTO checkAnswer(
            @RequestBody AnswerSubmissionDTO submission,
            @AuthenticationPrincipal UserDetails userDetails) {
        return learningService.processAnswer(submission, userDetails);
    }
}
