package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordServiceImpl {

    private final WordRepository wordRepository;
    private final S3Service s3Service;

    public Word addWord(String wordText, String definition,
                        MultipartFile audioFile, MultipartFile videoFile) throws Exception {

        String audioUrl = null;
        String videoUrl = null;

        if (audioFile != null && !audioFile.isEmpty()) {
            audioUrl = s3Service.uploadFile(audioFile);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            videoUrl = s3Service.uploadFile(videoFile);
        }

        Word word = new Word();
        word.setWord(wordText);
        word.setDefinition(definition);
        word.setAudioPath(audioUrl);
        word.setVideoPath(videoUrl);
        log.info("Adding word: {}", word);
        return wordRepository.save(word);
    }

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }
}
