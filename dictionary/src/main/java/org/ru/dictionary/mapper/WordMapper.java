package org.ru.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.Word;

@Mapper(componentModel = "spring")
public abstract class WordMapper {

    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "audioPath", source = "audioPath")
    @Mapping(target = "videoPath", source = "videoPath")
    @Mapping(target = "courseId", source = "level.course.id")
    @Mapping(target = "levelId", source = "level.id")
    public abstract WordResponseDTO toWordDto(Word word);

    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(WordRequestDTO dto, @MappingTarget Word entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "audioPath", source = "audioPath")
    @Mapping(target = "videoPath", source = "videoPath")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "level", source = "level")
    public abstract Word toEntity(WordRequestDTO dto, String imagePath, String audioPath, String videoPath, Integer orderNumber, Level level);
}

