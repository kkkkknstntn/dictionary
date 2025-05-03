package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.ru.dictionary.dto.word.WordRequestDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.mapper.WordMapper;
import org.ru.dictionary.repository.LevelRepository;
import org.ru.dictionary.repository.WordRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordServiceImpl {

    private final WordRepository wordRepository;
    private final LevelRepository levelRepository;
    private final WordMapper wordMapper;
    private final CourseServiceImpl courseService;
    private final S3Service s3Service;

    @Transactional
    public WordResponseDTO createWord(WordRequestDTO dto, UserDetails userDetails) throws IOException {
        Level level = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        courseService.checkAuthorOrAdmin(level.getCourse(), userDetails);

        String audioUrl = dto.getAudioFile() != null && !dto.getAudioFile().isEmpty() ?
                s3Service.uploadFile(dto.getAudioFile()) : null;

        String videoUrl = dto.getVideoFile() != null && !dto.getVideoFile().isEmpty() ?
                s3Service.uploadFile(dto.getVideoFile()) : null;

        String imageUrl = dto.getImageFile() != null && !dto.getImageFile().isEmpty() ?
                s3Service.uploadFile(dto.getImageFile()) : null;

        Integer lastOrderNumber = wordRepository.findTopByLevelIdOrderByOrderNumberDesc(dto.getLevelId()).map(Word::getOrderNumber).orElse(0);

        Word newWord = wordMapper.toEntity(dto, imageUrl, audioUrl, videoUrl, lastOrderNumber + 1, level);

        return wordMapper.toWordDto(wordRepository.save(newWord));
    }

    public WordResponseDTO getWordById(Long id) {
        return wordRepository.findById(id)
                .map(wordMapper::toWordDto)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));
    }

    public List<WordResponseDTO> getWordsByLevel(Long levelId) {
        return wordRepository.findByLevelId(levelId).stream()
                .map(wordMapper::toWordDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WordResponseDTO updateWord(Long id, WordRequestDTO dto, UserDetails userDetails) throws IOException {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse(), userDetails);

        Level newLevel = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        if (!newLevel.getCourse().getId().equals(word.getLevel().getCourse().getId())) {
             new BadRequestException("Cannot move word to another course");
        }

        wordMapper.updateFromDto(dto, word);

        String audioUrl = dto.getAudioFile() != null && !dto.getAudioFile().isEmpty() ?
                s3Service.uploadFile(dto.getAudioFile()) : null;

        String videoUrl = dto.getVideoFile() != null && !dto.getVideoFile().isEmpty() ?
                s3Service.uploadFile(dto.getVideoFile()) : null;

        String imageUrl = dto.getImageFile() != null && !dto.getImageFile().isEmpty() ?
                s3Service.uploadFile(dto.getImageFile()) : null;

        word.setLevel(newLevel);
        if (audioUrl != null) {
            word.setAudioPath(audioUrl);
        }
        if (videoUrl != null) {
            word.setVideoPath(videoUrl);
        }
        if (imageUrl != null) {
            word.setImagePath(imageUrl);
        }

        return wordMapper.toWordDto(wordRepository.save(word));
    }

    @Transactional
    public void deleteWord(Long id, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        courseService.checkAuthorOrAdmin(word.getLevel().getCourse(), userDetails);
        wordRepository.delete(word);
    }
}