package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.Authorities;
import org.ru.dictionary.mapper.CourseMapper;
import org.ru.dictionary.repository.CourseRepository;
import org.ru.dictionary.repository.UserRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;


    public void checkAuthorOrAdmin(Course course, UserDetails userDetails) {

        boolean isAuthor = course.getAuthor().getUsername().equals(userDetails.getUsername());

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authorities.ROLE_ADMIN.name()));

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("Access denied: you are not allowed to modify this course");
        }
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO dto, UserDetails userDetails) {
        User author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseMapper.toEntity(dto);
        course.setAuthor(author);
        course.setParticipants(resolveParticipants(dto.getParticipantIds()));

        return courseMapper.toResponseDTO(courseRepository.save(course));
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        checkAuthorOrAdmin(course, userDetails);

        courseMapper.updateFromDto(dto, course);
        course.setParticipants(resolveParticipants(dto.getParticipantIds()));

        return courseMapper.toResponseDTO(courseRepository.save(course));
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseResponseDTO> getUserCourses(UserDetails userDetails) {
        var user = userRepository.findByUsername(userDetails.getUsername());
        return courseRepository.findByParticipantsContaining(user.get()).stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourse(Long courseId, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        checkAuthorOrAdmin(course, userDetails);

        courseRepository.delete(course);
    }

    private Set<User> resolveParticipants(Set<Long> participantIds) {
        if (participantIds == null) return Collections.emptySet();

        return participantIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void joinCourse(Long courseId, UserDetails userDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!course.getParticipants().contains(user)) {
            course.getParticipants().add(user);
            courseRepository.save(course);
        }
    }
}