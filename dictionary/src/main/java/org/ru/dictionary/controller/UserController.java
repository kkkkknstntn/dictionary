package org.ru.dictionary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.user.UserRequestDTO;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.service.UserService;
import org.ru.dictionary.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(
            @Validated(ValidationGroups.Create.class) @RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @RequestBody UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }
}