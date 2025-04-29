package org.ru.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.enums.LearningType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningMaterialDTO {
    private WordResponseDTO targetWord;
    private List<String> options;
    private LearningType type;
}