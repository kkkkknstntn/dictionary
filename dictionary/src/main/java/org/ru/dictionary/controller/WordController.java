package org.ru.dictionary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.service.WordServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@Tag(name = "Words", description = "Управление словами и их содержимым")
public class WordController {

    private final WordServiceImpl wordService;


    @Operation(
            summary = "Создать новое слово",
            description = "Добавление слова с медиа-файлами (аудио, видео, изображение)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Слово успешно создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Нет прав на создание"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @Parameter(description = "Данные слова с файлами", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WordResponseDTO createWord(
            @ModelAttribute @Valid WordRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return wordService.createWord(request, userDetails);
    }

    @Operation(
            summary = "Получить слово по ID",
            description = "Публичный доступ к информации о слове"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о слове"),
            @ApiResponse(responseCode = "404", description = "Слово не найдено")
    })
    @GetMapping("/{id}")
    public WordResponseDTO getWordById(@PathVariable Long id) {
        return wordService.getWordById(id);
    }

    @Operation(
            summary = "Получить слова по уровню",
            description = "Список всех слов, привязанных к уровню"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список слов уровня"),
            @ApiResponse(responseCode = "404", description = "Уровень не найден")
    })
    @GetMapping("/level/{levelId}")
    public List<WordResponseDTO> getWordsByLevel(@PathVariable Long levelId) {
        return wordService.getWordsByLevel(levelId);
    }

    @Operation(
            summary = "Обновить слово",
            description = "Доступно автору курса или администратору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Слово обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
            @ApiResponse(responseCode = "404", description = "Слово или уровень не найдены")
    })
    @Parameter(description = "Данные слова с файлами",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PutMapping("/{id}")
    public WordResponseDTO updateWord(
            @PathVariable Long id,
            @Valid @ModelAttribute WordRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return wordService.updateWord(id, dto, userDetails);
    }

    @Operation(
            summary = "Удалить слово",
            description = "Доступно автору курса или администратору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Слово удалено"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Слово не найдено")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWord(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        wordService.deleteWord(id, userDetails);
    }
}