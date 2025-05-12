package org.ru.dictionary.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ru.dictionary.validation.ValidationGroups;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CourseRequestDTO {
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String title;

    @Size(max = 1000, groups = {ValidationGroups.Create.class})
    private String description;

    private MultipartFile imageFile;
}