package org.ru.dictionary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.service.ProgressService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "API для работы с прогрессом пользователя")
public class ProgressController {

    private final ProgressService progressService;

    @Operation(summary = "Получить средний прогресс по уровню")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Средний прогресс"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @GetMapping("/level/{levelId}")
    public ProgressAverageDTO getLevelProgress(
            @PathVariable Long levelId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.getAverageProgressForLevel(userDetails, levelId);
    }

    @Operation(summary = "Получить средний прогресс по курсу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Средний прогресс"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/course/{courseId}")
    public ProgressAverageDTO getCourseProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.getAverageProgressForCourse(userDetails, courseId);
    }
}