package org.ru.dictionary.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.sql.Update;
import org.ru.dictionary.validation.ValidationGroups;

import java.util.Set;

@Data
public class CourseRequestDTO {
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String title;

    @Size(max = 1000, groups = {ValidationGroups.Create.class})
    private String description;

    @NotNull(groups = ValidationGroups.Create.class)
    private Long authorId;

    private Set<Long> participantIds;
}