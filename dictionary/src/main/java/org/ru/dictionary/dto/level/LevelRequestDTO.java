package org.ru.dictionary.dto.level;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ru.dictionary.validation.ValidationGroups;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelRequestDTO {
    @NotBlank(message = "Level name is required", groups = {ValidationGroups.Create.class})
    private String name;

    @NotNull(message = "Order number is required", groups = {ValidationGroups.Update.class})
    @Null(groups = {ValidationGroups.Create.class})
    @Min(value = 1, message = "Order number must be at least 1")
    private Integer orderNumber;

    @NotBlank(groups = {ValidationGroups.Create.class})
    @Null(groups = {ValidationGroups.Update.class})
    private Long courseId;
}