package org.ru.dictionary.dto.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelResponseDTO {
    private Long id;
    private String name;
    private int orderNumber;
}
