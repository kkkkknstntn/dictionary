package org.ru.dictionary.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRequestDTO(
            @NotBlank
            @Size(min = 3, max = 20)
            String username,

            @NotBlank
            @Size(min = 8, max = 30)
            String password,

            Set<String> roles
    ) {}
