package org.ru.dictionary.dto.word;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ru.dictionary.validation.ValidationGroups;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordRequestDTO {
    @NotBlank(message = "word is required", groups = {ValidationGroups.Create.class})
    private String word;

    @NotBlank(message = "Word definition is required",groups = {ValidationGroups.Create.class})
    private String definition;

    @NotBlank(message = "LevelId is required", groups = {ValidationGroups.Create.class})
    private Long levelId;

    private boolean activeForTesting = true;

    private MultipartFile audioFile;
    private MultipartFile videoFile;
    private MultipartFile imageFile;
}