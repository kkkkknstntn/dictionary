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
import org.springframework.web.multipart.MultipartFile;

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
    public WordResponseDTO createWord(String word, String definition, Long levelId, UserDetails userDetails, MultipartFile audioFile, MultipartFile videoFile, MultipartFile imageFile) throws IOException {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        courseService.checkAuthor(level.getCourse(), userDetails);

        String audioUrl = null;
        String videoUrl = null;
        String imageUrl = null;

        if (audioFile != null && !audioFile.isEmpty()) {
            audioUrl = s3Service.uploadFile(audioFile);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            videoUrl = s3Service.uploadFile(videoFile);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadFile(imageFile);
        }

        Word newWord = Word.builder()
                .word(word)
                .level(level)
                .definition(definition)
                .audioPath(audioUrl)
                .videoPath(videoUrl)
                .imagePath(imageUrl)
                .build();

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
    public WordResponseDTO updateWord(Long id, WordRequestDTO dto, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        courseService.checkAuthor(word.getLevel().getCourse(), userDetails);

        Level newLevel = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        if (!newLevel.getCourse().getId().equals(word.getLevel().getCourse().getId())) {
             new BadRequestException("Cannot move word to another course");
        }

        wordMapper.updateFromDto(dto, word);
        word.setLevel(newLevel);
        return wordMapper.toWordDto(wordRepository.save(word));
    }

    @Transactional
    public void deleteWord(Long id, UserDetails userDetails) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        courseService.checkAuthor(word.getLevel().getCourse(), userDetails);
        wordRepository.delete(word);
    }
}