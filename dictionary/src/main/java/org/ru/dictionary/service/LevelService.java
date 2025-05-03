package org.ru.dictionary.service;

import org.ru.dictionary.dto.level.LevelRequestDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface LevelService {
    LevelResponseDTO createLevel(LevelRequestDTO dto, UserDetails userDetails);
    LevelResponseDTO getLevelById(Long levelId);
    List<LevelResponseDTO> getAllLevelsByCourse(Long courseId);
    LevelResponseDTO updateLevel(Long levelId, LevelRequestDTO dto, UserDetails userDetails);
    void deleteLevel(Long levelId, UserDetails userDetails);
}