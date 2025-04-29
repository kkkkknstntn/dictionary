package org.ru.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ru.dictionary.dto.user.UserResponseDTO;
import org.ru.dictionary.dto.course.CourseResponseDto;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Mapping(target = "author", source = "author", qualifiedByName = "toUserDto")
    @Mapping(target = "levels", source = "levels", qualifiedByName = "toLevelDtos")
    @Mapping(target = "participants", source = "participants", qualifiedByName = "toUserDtos")
    public abstract CourseResponseDto toCourseDto(Course course);

    @Named("toLevelDtos")
    protected List<LevelResponseDTO> mapLevels(List<Level> levels) {
        return levels.stream()
                .map(this::toLevelDto)
                .collect(Collectors.toList());
    }

    @Named("toLevelDto")
    public abstract LevelResponseDTO toLevelDto(Level level);

    @Named("toUserDtos")
    protected Set<UserResponseDTO> mapUsers(Set<User> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toSet());
    }

    @Named("toUserDto")
    public abstract UserResponseDTO toUserDto(User user);
}