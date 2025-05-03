package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.user.UserRequestDTO;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.enums.Authorities;
import org.ru.dictionary.exception.GlobalExceptionHandler;
import org.ru.dictionary.mapper.UserMapper;
import org.ru.dictionary.repository.UserRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new GlobalExceptionHandler.CategoryNotFoundException(
                    "Username '" + request.getUsername() + "' already exists");
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Optional.ofNullable(request.getRoles()).ifPresent(roles -> user.setRoles(getRolesFromNames(request.getRoles())));

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRoles() != null) {
            user.setRoles(userMapper.rolesToAuthorities(dto.getRoles()));
        }

        return userMapper.toResponseDTO(user);
    }

    private Set<Authorities> getRolesFromNames(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> {
                    try {
                        return Authorities.valueOf(name);
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalArgumentException("Invalid role name: " + name);
                    }
                })
                .collect(Collectors.toSet());
    }
}