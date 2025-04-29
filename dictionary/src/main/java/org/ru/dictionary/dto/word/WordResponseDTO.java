package org.ru.dictionary.dto.word;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponseDTO {
    private Long id;
    private String word;
    private String definition;
    private String imagePath;
    private String audioPath;
    private String videoPath;
}
