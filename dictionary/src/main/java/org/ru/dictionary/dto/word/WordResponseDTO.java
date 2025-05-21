package org.ru.dictionary.dto.word;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponseDTO implements Serializable {
    private Long id;
    private String word;
    private String definition;
    private String imagePath;
    private String audioPath;
    private String videoPath;
    private Long courseId;
    private Long levelId;
    private boolean activeForTesting;
}
