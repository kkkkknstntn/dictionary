package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.repository.ProgressRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.service.ProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateProgress(User user, Long wordId, int delta) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + wordId
                ));

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
        if (!userRepository.existsById(userId)) {
            throw new ApiException(
                    BusinessErrorCodes.USER_NOT_FOUND,
                    "User ID: " + userId
            );
        }

        return progressRepository.findByUserIdAndWordId(userId, wordId)
                .map(Progress::getProgressValue)
                .orElse(0);
    }
}