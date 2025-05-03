package org.ru.dictionary.service;

import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public interface WordService {
    WordResponseDTO createWord(WordRequestDTO dto, UserDetails userDetails);
    WordResponseDTO getWordById(Long id);
    List<WordResponseDTO> getWordsByLevel(Long levelId);
    WordResponseDTO updateWord(Long id, WordRequestDTO dto, UserDetails userDetails);
    void deleteWord(Long id, UserDetails userDetails);
}