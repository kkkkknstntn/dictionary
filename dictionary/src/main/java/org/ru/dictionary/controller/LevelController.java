package org.ru.dictionary.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.level.LevelRequestDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.service.LevelServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/levels")
@Validated
@RequiredArgsConstructor
@Tag(name = "Levels", description = "Управление уровнями курсов")
public class LevelController {

    private final LevelServiceImpl levelService;

    @Operation(
            summary = "Создать новый уровень",
            description = "Доступно автору курса или администратору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Уровень успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Нет прав на создание"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelResponseDTO createLevel(
            @Valid @RequestBody LevelRequestDTO dto,
            @AuthenticationPrincipal UserDetails currentUser) {
        return levelService.createLevel(dto, currentUser);
    }

    @Operation(
            summary = "Получить уровни курса",
            description = "Публичный доступ к списку уровней курса"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список уровней"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping
    public List<LevelResponseDTO> getAllLevels(@PathVariable Long courseId) {
        return levelService.getAllLevelsByCourse(courseId);
    }

    @Operation(
            summary = "Получить уровень по ID",
            description = "Публичный доступ к информации об уровне"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация об уровне"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @GetMapping("/{levelId}")
    public LevelResponseDTO getLevel(
            @PathVariable Long levelId) {
        return levelService.getLevelById(levelId);
    }

    @Operation(
            summary = "Обновить уровень",
            description = "Доступно автору курса или администратору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уровень обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @PutMapping("/{levelId}")
    public LevelResponseDTO updateLevel(
            @PathVariable Long levelId,
            @Valid @RequestBody LevelRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return levelService.updateLevel(levelId, dto, userDetails);
    }

    @Operation(
            summary = "Удалить уровень",
            description = "Доступно автору курса или администратору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Уровень удален"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @DeleteMapping("/{levelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLevel(
            @PathVariable Long levelId,
            @AuthenticationPrincipal UserDetails userDetails) {
        levelService.deleteLevel(levelId, userDetails);
    }
}