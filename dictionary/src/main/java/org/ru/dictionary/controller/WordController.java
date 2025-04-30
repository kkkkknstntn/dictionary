package org.ru.dictionary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.service.WordServiceImpl;
import org.springframework.http.HttpStatus;
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
public class WordController {

    private final WordServiceImpl wordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WordResponseDTO createWord(
            @RequestParam String word,
            @RequestParam String definition,
            @RequestParam Long levelId,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        return wordService.createWord(word, definition, levelId, userDetails, audioFile, videoFile, imageFile);
    }

    @GetMapping("/{id}")
    public WordResponseDTO getWordById(@PathVariable Long id) {
        return wordService.getWordById(id);
    }

    @GetMapping("/level/{levelId}")
    public List<WordResponseDTO> getWordsByLevel(@PathVariable Long levelId) {
        return wordService.getWordsByLevel(levelId);
    }

    @PutMapping("/{id}")
    public WordResponseDTO updateWord(
            @PathVariable Long id,
            @Valid @RequestBody WordRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return wordService.updateWord(id, dto, userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWord(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        wordService.deleteWord(id, userDetails);
    }
}