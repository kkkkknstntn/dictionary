package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.level.LevelRequestDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.mapper.LevelMapper;
import org.ru.dictionary.repository.CourseRepository;
import org.ru.dictionary.repository.LevelRepository;
import org.ru.dictionary.service.CourseService;
import org.ru.dictionary.service.LevelService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final LevelMapper levelMapper;

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "levels", key = "#dto.courseId"),
//            @CacheEvict(value = "courseLevels", key = "#dto.courseId"),
//            @CacheEvict(value = "words", allEntries = true)
//    })
    public LevelResponseDTO createLevel(LevelRequestDTO dto, UserDetails userDetails) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + dto.getCourseId()
                ));

        courseService.checkAuthorOrAdmin(course);

        Level level = levelMapper.toEntity(dto);
        level.setCourse(course);
        level.setOrderNumber(calculateNextOrderNumber(course));

        return levelMapper.toResponseDTO(levelRepository.save(level));
    }

    //////@Cacheable(value = "levelDetails", key = "#levelId")
    public LevelResponseDTO getLevelById(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + levelId
                ));
        return levelMapper.toResponseDTO(level);
    }

    //////@Cacheable(value = "courseLevels", key = "#courseId")
    public List<LevelResponseDTO> getAllLevelsByCourse(Long courseId) {
        return levelRepository.findByCourseIdOrderByOrderNumberAsc(courseId).stream()
                .map(levelMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "levelDetails", key = "#levelId"),
//            @CacheEvict(value = "words", allEntries = true)
//    })
    public LevelResponseDTO updateLevel(Long levelId, LevelRequestDTO dto, UserDetails userDetails) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + levelId
                ));

        courseService.checkAuthorOrAdmin(level.getCourse());

        levelMapper.updateFromDto(dto, level);
        return levelMapper.toResponseDTO(levelRepository.save(level));
    }

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "levelDetails", key = "#levelId"),
//            @CacheEvict(value = "words", allEntries = true),
//            @CacheEvict(value = "userLearningMaterials", allEntries = true)
//    })
    public void deleteLevel(Long levelId, UserDetails userDetails) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + levelId
                ));

        courseService.checkAuthorOrAdmin(level.getCourse());

        levelRepository.delete(level);
        reorderLevelsAfterDeletion(level.getCourse());
    }

    private int calculateNextOrderNumber(Course course) {
        return levelRepository.findByCourseIdOrderByOrderNumberDesc(course.getId())
                .stream()
                .findFirst()
                .map(lastLevel -> lastLevel.getOrderNumber() + 1)
                .orElse(1);
    }

    private void reorderLevelsAfterDeletion(Course course) {
        List<Level> levels = levelRepository.findByCourseIdOrderByOrderNumberAsc(course.getId());
        for (int i = 0; i < levels.size(); i++) {
            levels.get(i).setOrderNumber(i + 1);
        }
        levelRepository.saveAll(levels);
    }
}