package org.ru.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.ru.dictionary.dto.level.LevelRequestDTO;
import org.ru.dictionary.dto.level.LevelResponseDTO;
import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.Level;

@Mapper(componentModel = "spring")
public abstract class LevelMapper {
    @Mapping(target = "course", source = "courseId", qualifiedByName = "mapCourseIdToCourse")
    public abstract Level toEntity(LevelRequestDTO dto);

    @Mapping(target = "courseId", source = "course.id")
    public abstract LevelResponseDTO toResponseDTO(Level level);

    @Named("mapCourseIdToCourse")
    protected Course mapCourseIdToCourse(Long courseId) {
        return Course.builder().id(courseId).build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    public abstract void updateFromDto(LevelRequestDTO dto, @MappingTarget Level entity);
}