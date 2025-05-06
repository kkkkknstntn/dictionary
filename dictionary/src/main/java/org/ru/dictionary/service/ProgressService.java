package org.ru.dictionary.service;

import org.ru.dictionary.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface ProgressService {
    void updateProgress(User user, Long wordId, int delta);
    Integer getProgress(Long userId, Long wordId);
    Double getAverageProgressForLevel(UserDetails userDetails, Long levelId);
    Double getAverageProgressForCourse(UserDetails userDetails, Long courseId);
}