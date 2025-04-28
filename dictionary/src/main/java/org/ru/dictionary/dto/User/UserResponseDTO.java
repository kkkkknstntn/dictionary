package org.ru.dictionary.dto.User;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
    Long id,
    String username,
    Set<String> roles,
    LocalDateTime createdAt
) {}
