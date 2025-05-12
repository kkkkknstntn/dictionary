package org.ru.dictionary.dto.course;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String imagePath;
    private UserResponseDTO author;
    private List<LevelResponseDTO> levels;
    private Set<UserResponseDTO> participants;
    private LocalDateTime createdAt;
}
