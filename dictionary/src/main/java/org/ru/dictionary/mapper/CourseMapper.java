package org.ru.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.ru.dictionary.document.CourseDocument;
import org.ru.dictionary.dto.course.CourseRequestDTO;
import org.ru.dictionary.dto.course.CourseResponseDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "participants", ignore = true)
    public abstract Course toEntity(CourseRequestDTO dto);

    @Mapping(target = "author", source = "author", qualifiedByName = "toUserDto")
    @Mapping(target = "levels", source = "levels", qualifiedByName = "toLevelDtos")
    @Mapping(target = "participants", source = "participants", qualifiedByName = "toUserDtos")
    public abstract CourseResponseDTO toResponseDTO(Course course);

    @Named("toLevelDtos")
    protected List<LevelResponseDTO> toLevelDtos(List<Level> levels) {
        return Optional.ofNullable(levels)
                .map(levelsList -> levelsList
                        .stream()
                        .map(this::toLevelDto)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    @Named("toLevelDto")
    public abstract LevelResponseDTO toLevelDto(Level level);

    @Named("toUserDtos")
    protected Set<UserResponseDTO> toUserDtos(Set<User> users) {
        return Optional.ofNullable(users)
                        .map(usersList -> usersList
                                .stream()
                .map(this::toUserDto)
                                .collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);
    }

    @Named("toUserDto")
    public abstract UserResponseDTO toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "participants", ignore = true)
    public abstract void updateFromDto(CourseRequestDTO dto, @MappingTarget Course entity);

    public abstract CourseResponseDTO toDto(CourseDocument course);
}
