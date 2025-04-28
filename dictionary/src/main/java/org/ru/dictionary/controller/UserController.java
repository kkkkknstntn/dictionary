package org.ru.dictionary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.User.UserRequestDTO;
import org.ru.dictionary.dto.User.UserResponseDTO;
import org.ru.dictionary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO registerUser(
            @Valid @RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }
}