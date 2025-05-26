package org.ru.dictionary.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.dictionary.document.CourseDocument;
import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.Authorities;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.mapper.CourseMapper;
import org.ru.dictionary.mapper.UserMapper;
import org.ru.dictionary.repository.CourseDocumentRepository;
import org.ru.dictionary.repository.CourseRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.service.CourseService;
import org.ru.dictionary.service.ProgressService;
import org.ru.dictionary.service.S3Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private final CourseDocumentRepository courseDocumentRepository;
    private final UserMapper userMapper;
    private final S3Service s3Service;
    private final ProgressService progressService;

    public void checkAuthorOrAdmin(Course course) {
        boolean isAuthor = course.getAuthor().getUsername().equals( SecurityContextHolder.getContext().getAuthentication().getName());
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authorities.ROLE_ADMIN.name()));

        if (!isAuthor && !isAdmin) {
            throw new ApiException(BusinessErrorCodes.COURSE_ACCESS_DENIED);
        }
    }

    //@Cacheable("allCourses")
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    //@Cacheable(value = "courses", key = "#query")
    public List<CourseResponseDTO> getCourses(String query) {
        List<Long> courseIds = courseDocumentRepository.searchCourses(query)
                .stream()
                .map(CourseDocument::getId)
                .collect(Collectors.toList());

        if (courseIds.isEmpty()) {
            return Collections.emptyList();
        }

        return courseRepository.findAllById(courseIds).stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    @CacheEvict(value = {"allCourses", "courses"}, allEntries = true)
    public CourseResponseDTO createCourse(CourseRequestDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND));

        String imageUrl = s3Service.uploadFile(dto.getImageFile());

        Course course = courseMapper.toEntity(dto);
        Set<User> participants = new HashSet<>();
        participants.add(author);

        course.setParticipants(participants);
        course.setAuthor(author);
        course.setImagePath(imageUrl);

        return courseMapper.toResponseDTO(courseRepository.save(course));
    }


    @Transactional
    @CacheEvict(value = {"allCourses", "courses"}, allEntries = true)
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND));

        checkAuthorOrAdmin(course);

        if(dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String newImageUrl = s3Service.uploadFile(dto.getImageFile());
            course.setImagePath(newImageUrl);
        }

        courseMapper.updateFromDto(dto, course);
        return courseMapper.toResponseDTO(courseRepository.save(course));
    }


    @Transactional
    @CacheEvict(value = {"allCourses", "courses"}, allEntries = true)
    public List<CourseResponseDTO> getUserCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(user -> courseRepository.findByParticipantsContaining(user)
                        .stream()
                        .map(courseMapper::toResponseDTO)
                        .toList())
                .orElse(Collections.emptyList());
    }


    @Transactional
    @CacheEvict(value = {"allCourses", "courses", "userCourses"}, allEntries = true)
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + courseId));

        checkAuthorOrAdmin(course);
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
    @CacheEvict(value = "userCourses", key = "#userDetails.username")
    public CourseResponseDTO joinCourse(Long courseId, UserDetails userDetails) {
        Course course = courseRepository.findByIdWithParticipants(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + courseId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.USER_NOT_FOUND,
                        "User: " + username));

        if (!course.getParticipants().contains(user)) {
            course.getParticipants().add(user);
             return courseMapper.toResponseDTO(courseRepository.save(course));
        }
        throw new ApiException(BusinessErrorCodes.USER_ALREADY_EXISTS,  "Course ID: " + courseId);
    }

    @Transactional
    //@Cacheable(value = "courseProgress", key = "#courseId")
    public List<ProgressAverageDTO> getCourseUserProgress(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND, "Course ID: " + courseId));

        Set<User> participants = course.getParticipants();

        return participants.stream()
                .map(user -> progressService.getAverageProgressForCourse(userMapper.toResponseDTO(user), courseId))
                .collect(Collectors.toList());
    }

    //@Cacheable(value = "courses", key = "#id")
    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.COURSE_NOT_FOUND,
                        "Course ID: " + id));
        return courseMapper.toResponseDTO(course);
    }
}