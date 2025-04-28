package org.ru.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import org.ru.dictionary.dto.User.UserResponseDTO;
import org.ru.dictionary.entity.Role;
import org.ru.dictionary.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    public abstract UserResponseDTO map(User user);

    @Named("roleToString")
    protected static String roleToString(Role role) {
        return role.getName();
    }

    @Named("rolesToStrings")
    protected Set<String> rolesToStrings(Set<Role> roles) {
        return Optional.ofNullable(roles)
                .stream()
                .flatMap(Set::stream)
                .map(UserMapper::roleToString)
                .collect(Collectors.toSet());
    }
}
