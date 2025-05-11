package org.ru.dictionary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ru.dictionary.dto.user.UserResponseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Среднее значение прогресса")
public class ProgressAverageDTO {
    private UserResponseDTO user;
    @Schema(description = "Средний прогресс", example = "65.5")
    private Double averageProgress;
}