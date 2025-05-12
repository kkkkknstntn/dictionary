package org.ru.dictionary.service;

import org.ru.dictionary.dto.user.UserRequestDTO;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO updateUser(Long id, UserRequestDTO dto);
    UserResponseDTO getCurrentUser();
}