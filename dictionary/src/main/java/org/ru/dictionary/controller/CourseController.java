package org.ru.dictionary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.ProgressAverageDTO;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.service.CourseService;
import org.ru.dictionary.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Управление курсами и их содержимым")
@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(
            summary = "Создать новый курс",
            description = "Доступно только авторизованным пользователям с ролью автора"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные курса"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponseDTO createCourse(
            @Validated(ValidationGroups.Create.class) @ModelAttribute CourseRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return courseService.createCourse(dto, userDetails);
    }

    @Operation(summary = "Обновить курс", description = "Доступно только автору курса")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс обновлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CourseResponseDTO updateCourse(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @ModelAttribute CourseRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return courseService.updateCourse(id, dto, userDetails);
    }

    @Operation(summary = "Получить все курсы", description = "Публичный доступ")
    @ApiResponse(responseCode = "200", description = "Список всех курсов")
    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @Operation(
            summary = "Получить курсы пользователя",
            description = "Возвращает курсы, в которых участвует текущий пользователь"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список курсов"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
    })
    @GetMapping("/my-courses")
    public List<CourseResponseDTO> getUserCourses(
            @AuthenticationPrincipal UserDetails userDetails){
        return courseService.getUserCourses(userDetails);
    }

    @Operation(
            summary = "Удалить курс",
            description = "Доступно только автору курса"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс удален"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.deleteCourse(id, userDetails);
    }


    @Operation(
            summary = "Вступить в курс",
            description = "Добавляет текущего пользователя в участники курса"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное вступление в курс"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.OK)
    public void joinCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.joinCourse(id, userDetails);
    }

    @Operation(summary = "Получить курсы по строке", description = "Публичный доступ")
    @ApiResponse(responseCode = "200", description = "Список курсов")
    @GetMapping("/search")
    public List<CourseResponseDTO> getCourses(@RequestParam String query) {
        return courseService.getCourses(query);
    }

    @Operation(
            summary = "Получить пользователей курса с их средним прогрессом",
            description = "Возвращает список пользователей курса с их средним прогрессом"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей и их прогресса по курсу"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}/progress")
    public List<ProgressAverageDTO> getCourseUserProgress(
            @PathVariable Long id) {
        return courseService.getCourseUserProgress(id);
    }

    @Operation(summary = "Получить курс по ID", description = "Публичный доступ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public CourseResponseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }
}