package org.ru.dictionary.service;

import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface ProgressService {
    void updateProgress(User user, Long wordId, int delta);
    Integer getProgress(Long userId, Long wordId);
    ProgressAverageDTO getProgress(UserDetails userDetails, Long wordId);
    ProgressAverageDTO getAverageProgressForLevel(UserDetails userDetails, Long levelId);
    ProgressAverageDTO getAverageProgressForCourse(UserDetails userDetails, Long courseId);

    ProgressAverageDTO getAverageProgressForCourse(UserResponseDTO userDTO, Long courseId);
}