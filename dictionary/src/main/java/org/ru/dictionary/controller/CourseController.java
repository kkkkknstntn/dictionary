package org.ru.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.service.CourseServiceImpl;
import org.ru.dictionary.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponseDTO createCourse(
            @Validated(ValidationGroups.Create.class) @RequestBody CourseRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return courseService.createCourse(dto, userDetails);
    }

    @PutMapping("/{id}")
    public CourseResponseDTO updateCourse(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @RequestBody CourseRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return courseService.updateCourse(id, dto, userDetails);
    }

    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/my-courses")
    public List<CourseResponseDTO> getUserCourses(
            @AuthenticationPrincipal UserDetails userDetails){
        return courseService.getUserCourses(userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.deleteCourse(id, userDetails);
    }
}