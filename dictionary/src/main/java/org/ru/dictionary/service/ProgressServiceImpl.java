package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.repository.ProgressRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.repository.WordRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProgressServiceImpl {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    @Transactional
    public void updateProgress(User user, Long wordId, int delta) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found with id: " + wordId));

        Progress progress = progressRepository.findByUserIdAndWordId(user.getId(), word.getId())
                .orElseGet(() -> Progress.builder()
                        .user(user)
                        .word(word)
                        .progressValue(0)
                        .build());

        int newValue = progress.getProgressValue() + delta;
        progress.setProgressValue(Math.min(100, Math.max(0, newValue)));

        progressRepository.save(progress);
    }

    public Integer getProgress(Long userId, Long wordId) {
        return progressRepository.findByUserIdAndWordId(userId, wordId)
                .map(Progress::getProgressValue)
                .orElse(0);
    }
}