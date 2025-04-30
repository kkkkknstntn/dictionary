package org.ru.dictionary.dto.word;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordRequestDTO {
    @NotBlank
    private String word;

    @NotBlank
    private String definition;
    @NotNull
    private Long levelId;

    private boolean activeForTesting = true;
}