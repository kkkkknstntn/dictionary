package org.ru.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.service.WordServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
public class WordController {
    private final WordRepository wordRepository;
    private final WordServiceImpl wordService;

    @GetMapping
    public List<Word> all() {
        return wordRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Word> addWord(
            @RequestParam String word,
            @RequestParam String definition,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile
    ) throws Exception {
        System.out.println(word);
        System.out.println(definition);
        Word savedWord = wordService.addWord(word, definition, audioFile, videoFile);
        return ResponseEntity.ok(savedWord);
    }
}
