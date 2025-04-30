package org.ru.dictionary.controller;


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
public class LevelController {

    private final LevelServiceImpl levelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelResponseDTO createLevel(
            @Valid @RequestBody LevelRequestDTO dto,
            @AuthenticationPrincipal UserDetails currentUser) {
        return levelService.createLevel(dto, currentUser);
    }

    @GetMapping
    public List<LevelResponseDTO> getAllLevels(@PathVariable Long courseId) {
        return levelService.getAllLevelsByCourse(courseId);
    }

    @GetMapping("/{levelId}")
    public LevelResponseDTO getLevel(
            @PathVariable Long levelId) {
        return levelService.getLevelById(levelId);
    }

    @PutMapping("/{levelId}")
    public LevelResponseDTO updateLevel(
            @PathVariable Long levelId,
            @Valid @RequestBody LevelRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return levelService.updateLevel(levelId, dto, userDetails);
    }

    @DeleteMapping("/{levelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLevel(
            @PathVariable Long levelId,
            @AuthenticationPrincipal UserDetails userDetails) {
        levelService.deleteLevel(levelId, userDetails);
    }
}