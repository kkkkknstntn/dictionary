package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.mapper.WordMapper;
import org.ru.dictionary.repository.LevelRepository;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.service.CourseService;
import org.ru.dictionary.service.S3Service;
import org.ru.dictionary.service.WordService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final LevelRepository levelRepository;
    private final WordMapper wordMapper;
    private final CourseService courseService;
    private final S3Service s3Service;

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "wordDetails", key = "#result.id"),
//            @CacheEvict(value = "levelWords", key = "#dto.levelId"),
//            @CacheEvict(value = "userLearningMaterials", allEntries = true)
//    })
    public WordResponseDTO createWord(WordRequestDTO dto, UserDetails userDetails) {
        Level level = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + dto.getLevelId()
                ));

        courseService.checkAuthorOrAdmin(level.getCourse());

        String audioUrl = s3Service.uploadFile(dto.getAudioFile());
        String videoUrl = s3Service.uploadFile(dto.getVideoFile());
        String imageUrl = s3Service.uploadFile(dto.getImageFile());

        int lastOrderNumber = wordRepository.findTopByLevelIdOrderByOrderNumberDesc(dto.getLevelId())
                .map(Word::getOrderNumber)
                .orElse(0);

        Word newWord = wordMapper.toEntity(dto, imageUrl, audioUrl, videoUrl, lastOrderNumber + 1, level);
        return wordMapper.toWordDto(wordRepository.save(newWord));
    }

    //@Cacheable(value = "wordDetails", key = "#id")
    public WordResponseDTO getWordById(Long id) {
        return wordRepository.findById(id)
                .map(wordMapper::toWordDto)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));
    }

    //@Cacheable(value = "levelWords", key = "#levelId")
    public List<WordResponseDTO> getWordsByLevel(Long levelId) {
        if (!levelRepository.existsById(levelId)) {
            throw new ApiException(
                    BusinessErrorCodes.LEVEL_NOT_FOUND,
                    "Level ID: " + levelId
            );
        }

        return wordRepository.findByLevelId(levelId).stream()
                .map(wordMapper::toWordDto)
                .collect(Collectors.toList());
    }

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "wordDetails", key = "#id"),
//            @CacheEvict(value = "userWordProgress", key = "{#userDetails.username, #id}")
//    })
    public WordResponseDTO updateWord(Long id, WordRequestDTO dto, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse());

        Level newLevel = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + dto.getLevelId()
                ));

        if (!newLevel.getCourse().getId().equals(word.getLevel().getCourse().getId())) {
            throw new ApiException(
                    BusinessErrorCodes.INVALID_OPERATION,
                    "Cannot move word to another course"
            );
        }

        wordMapper.updateFromDto(dto, word);
        updateMediaFiles(dto, word);
        return wordMapper.toWordDto(wordRepository.save(word));
    }

    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "wordDetails", key = "#id"),
//            @CacheEvict(value = "userWordProgress", key = "{#userDetails.username, #id}")
//    })
    public void deleteWord(Long id, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse());
        wordRepository.delete(word);
    }

    private void updateMediaFiles(WordRequestDTO dto, Word word) {
        Optional.ofNullable(dto.getAudioFile())
                .filter(file -> !file.isEmpty())
                .ifPresent(file -> word.setAudioPath(s3Service.uploadFile(file)));

        Optional.ofNullable(dto.getVideoFile())
                .filter(file -> !file.isEmpty())
                .ifPresent(file -> word.setVideoPath(s3Service.uploadFile(file)));

        Optional.ofNullable(dto.getImageFile())
                .filter(file -> !file.isEmpty())
                .ifPresent(file -> word.setImagePath(s3Service.uploadFile(file)));
    }
}