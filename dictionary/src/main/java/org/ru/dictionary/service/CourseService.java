package org.ru.dictionary.service;

import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.Course;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO dto);
    CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto);
    List<CourseResponseDTO> getAllCourses();
    List<CourseResponseDTO> getCourses(String query);
    List<CourseResponseDTO> getUserCourses();
    void deleteCourse(Long courseId);
    void joinCourse(Long courseId, UserDetails userDetails);
    void checkAuthorOrAdmin(Course course);

    List<ProgressAverageDTO> getCourseUserProgress(Long courseId);

    @Cacheable(value = "courses", key = "#id")
    CourseResponseDTO getCourseById(Long id);
}