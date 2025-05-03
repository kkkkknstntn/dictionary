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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public WordResponseDTO createWord(WordRequestDTO dto, UserDetails userDetails) {
        Level level = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + dto.getLevelId()
                ));

        courseService.checkAuthorOrAdmin(level.getCourse(), userDetails);

        String audioUrl = processFileUpload(dto.getAudioFile());
        String videoUrl = processFileUpload(dto.getVideoFile());
        String imageUrl = processFileUpload(dto.getImageFile());

        int lastOrderNumber = wordRepository.findTopByLevelIdOrderByOrderNumberDesc(dto.getLevelId())
                .map(Word::getOrderNumber)
                .orElse(0);

        Word newWord = wordMapper.toEntity(dto, imageUrl, audioUrl, videoUrl, lastOrderNumber + 1, level);
        return wordMapper.toWordDto(wordRepository.save(newWord));
    }

    public WordResponseDTO getWordById(Long id) {
        return wordRepository.findById(id)
                .map(wordMapper::toWordDto)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));
    }

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
    public WordResponseDTO updateWord(Long id, WordRequestDTO dto, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse(), userDetails);

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
    public void deleteWord(Long id, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + id
                ));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse(), userDetails);
        wordRepository.delete(word);
    }

    private String processFileUpload(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return s3Service.uploadFile(file);
        }
        return null;
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