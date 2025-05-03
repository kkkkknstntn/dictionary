package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.level.LevelRequestDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.mapper.LevelMapper;
import org.ru.dictionary.repository.CourseRepository;
import org.ru.dictionary.repository.LevelRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl {

    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;
    private final CourseServiceImpl courseService;
    private final LevelMapper levelMapper;

    @Transactional
    public LevelResponseDTO createLevel(LevelRequestDTO dto, UserDetails userDetails) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        courseService.checkAuthorOrAdmin(course, userDetails);

        Level level = levelMapper.toEntity(dto);
        level.setCourse(course);
        level.setOrderNumber(calculateNextOrderNumber(course));

        return levelMapper.toResponseDTO(levelRepository.save(level));
    }

    public LevelResponseDTO getLevelById(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));
        return levelMapper.toResponseDTO(level);
    }

    public List<LevelResponseDTO> getAllLevelsByCourse(Long courseId) {
        return levelRepository.findByCourseIdOrderByOrderNumberAsc(courseId).stream()
                .map(levelMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LevelResponseDTO updateLevel(Long levelId, LevelRequestDTO dto, UserDetails userDetails) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        courseService.checkAuthorOrAdmin(level.getCourse(), userDetails);

        levelMapper.updateFromDto(dto, level);
        return levelMapper.toResponseDTO(levelRepository.save(level));
    }

    @Transactional
    public void deleteLevel(Long levelId, UserDetails userDetails) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        courseService.checkAuthorOrAdmin(level.getCourse(), userDetails);

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
