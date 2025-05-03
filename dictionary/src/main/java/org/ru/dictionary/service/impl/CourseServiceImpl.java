package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.Authorities;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.mapper.CourseMapper;
import org.ru.dictionary.repository.CourseRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.service.CourseService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public void checkAuthorOrAdmin(Course course, UserDetails userDetails) {
        boolean isAuthor = course.getAuthor().getUsername().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authorities.ROLE_ADMIN.name()));

        if (!isAuthor && !isAdmin) {
            throw new ApiException(BusinessErrorCodes.COURSE_ACCESS_DENIED);
        }
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO dto, UserDetails userDetails) {
        User author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND,
                        "User not found: " + userDetails.getUsername()));

        Course course = courseMapper.toEntity(dto);
        course.setAuthor(author);

        return courseMapper.toResponseDTO(courseRepository.save(course));
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + courseId));

        checkAuthorOrAdmin(course, userDetails);

        courseMapper.updateFromDto(dto, course);

        return courseMapper.toResponseDTO(courseRepository.save(course));
    }

    public List<CourseResponseDTO> getUserCourses(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .map(user -> courseRepository.findByParticipantsContaining(user)
                        .stream()
                        .map(courseMapper::toResponseDTO)
                        .toList())
                .orElse(Collections.emptyList());
    }


    @Transactional
    public void deleteCourse(Long courseId, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + courseId));

        checkAuthorOrAdmin(course, userDetails);
        courseRepository.delete(course);
    }

    private Set<User> resolveParticipants(Set<Long> participantIds) {
        if (participantIds == null) return Collections.emptySet();

        return participantIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND,
                                "User ID: " + id)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void joinCourse(Long courseId, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + courseId));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND,
                        "User: " + userDetails.getUsername()));

        if (!course.getParticipants().contains(user)) {
            course.getParticipants().add(user);
            courseRepository.save(course);
        }
    }
}