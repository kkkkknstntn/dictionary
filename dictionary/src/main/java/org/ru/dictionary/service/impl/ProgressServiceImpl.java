package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.repository.*;
import org.ru.dictionary.service.ProgressService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final WordRepository wordRepository;
    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "userWordProgress", key = "{#user.id, #wordId}")
    @Caching(evict = {
            @CacheEvict(value = "words", key = "#wordId"),
            @CacheEvict(value = "userWordProgress", key = "{#user.id, #wordId}")
    })
    public void updateProgress(User user, Long wordId, int delta) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + wordId
                ));

        Progress progress = progressRepository.findByUserIdAndWordId(user.getId(), word.getId())
                .orElseGet(() -> Progress.builder()
                        .user(user)
                        .word(word)
                        .progressValue(0)
                        .build());

        int newValue = progress.getProgressValue() + delta;
        progress.setProgressValue(Math.min(100, Math.max(0, newValue)));

        progressRepository.save(progress);
    }

    @Cacheable(value = "userWordProgress", key = "{#userId, #wordId}")
    public Integer getProgress(Long userId, Long wordId) {
        if (!userRepository.existsById(userId)) {
            throw new ApiException(
                    BusinessErrorCodes.USER_NOT_FOUND,
                    "User ID: " + userId
            );
        }

        return progressRepository.findByUserIdAndWordId(userId, wordId)
                .map(Progress::getProgressValue)
                .orElse(0);
    }

    @Override
    public ProgressAverageDTO getProgress(UserDetails userDetails, Long wordId) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND, "User not found"));
        wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + wordId
                ));
        Double progressValue = getProgress(user.getId(), wordId).doubleValue();

        return new ProgressAverageDTO(progressValue);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressAverageDTO getAverageProgressForLevel(UserDetails userDetails, Long levelId) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND, "User not found"));

        levelRepository.findById(levelId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.LEVEL_NOT_FOUND, "Level ID: " + levelId));


        return new ProgressAverageDTO(getAverageProgressForLevel(user.getId(), levelId));
    }

    private  Double getAverageProgressForLevel(Long userId, Long levelId) {
        Integer totalWords = wordRepository.countByLevelId(levelId);

        Integer totalProgressForLevel = progressRepository.getTotalProgressForLevel(userId, levelId);

        return totalWords == 0 ? 0.0 : (double) totalProgressForLevel / totalWords;
    }

    @Override
    public ProgressAverageDTO getAverageProgressForCourse(UserDetails userDetails, Long courseId) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND, "User not found"));

        courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND, "Course ID: " + courseId));

        Double averageProgress = levelRepository.findByCourseId(courseId).stream().mapToDouble(
                level -> getAverageProgressForLevel(user.getId(), level.getId())
        ).average().orElse(0);
        return new ProgressAverageDTO(averageProgress);
    }
}