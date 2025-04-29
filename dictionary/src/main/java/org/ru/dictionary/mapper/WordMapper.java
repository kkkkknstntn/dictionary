package org.ru.dictionary.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.Word;

@Mapper(componentModel = "spring")
public abstract class WordMapper {
    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "audioPath", source = "audioPath")
    @Mapping(target = "videoPath", source = "videoPath")
    public abstract WordResponseDTO toWordDto(Word word);

    @InheritInverseConfiguration
    public abstract Word toWord(WordResponseDTO dto);
}
