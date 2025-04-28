package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.User.UserRequestDTO;
import org.ru.dictionary.dto.User.UserResponseDTO;
import org.ru.dictionary.entity.Role;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.exception.GlobalExceptionHandler;
import org.ru.dictionary.mapper.UserMapper;
import org.ru.dictionary.repository.RoleRepository;
import org.ru.dictionary.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new GlobalExceptionHandler.CategoryNotFoundException(
                    "Username '" + request.username() + "' already exists");
        });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        Optional.ofNullable(request.roles()).ifPresent(roles -> user.setRoles(getRolesFromNames(request.roles())));

        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser);
    }

    private Set<Role> getRolesFromNames(Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName)))
                .collect(Collectors.toSet());
    }
}