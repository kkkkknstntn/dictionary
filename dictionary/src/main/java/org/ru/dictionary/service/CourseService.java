package org.ru.dictionary.service;

import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.Course;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO dto, UserDetails userDetails);
    CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto, UserDetails userDetails);
    List<CourseResponseDTO> getAllCourses();
    List<CourseResponseDTO> getCourses(String query);
    List<CourseResponseDTO> getUserCourses(UserDetails userDetails);
    void deleteCourse(Long courseId, UserDetails userDetails);
    void joinCourse(Long courseId, UserDetails userDetails);
    void checkAuthorOrAdmin(Course course, UserDetails userDetails);

    List<ProgressAverageDTO> getCourseUserProgress(Long courseId);
}