package org.ru.dictionary.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.entity.User;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.enums.LearningType;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.mapper.WordMapper;
import org.ru.dictionary.repository.LevelRepository;
import org.ru.dictionary.repository.ProgressRepository;
import org.ru.dictionary.repository.UserRepository;
import org.ru.dictionary.repository.WordRepository;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.service.LearningService;
import org.ru.dictionary.service.ProgressService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {

    private final ProgressService progressService;
    private final LevelRepository levelRepository;
    private final WordRepository wordRepository;
    private final WordMapper wordMapper;
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Transactional
    public AnswerResultDTO processAnswer(AnswerSubmissionDTO submission, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.USER_NOT_FOUND,
                        "User not found: " + userDetails.getUsername()
                ));

        boolean isCorrect = validateAnswer(
                submission.getWordId(),
                submission.getAnswer(),
                submission.getType()
        );

        progressService.updateProgress(
                user,
                submission.getWordId(),
                isCorrect ? 10 : -5
        );

        int newProgress = progressService.getProgress(user.getId(), submission.getWordId());

        return new AnswerResultDTO(isCorrect, newProgress);
    }

    private boolean validateAnswer(Long wordId, String answer, LearningType type) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.WORD_NOT_FOUND,
                        "Word ID: " + wordId
                ));

        return switch (type) {
            case WORD_TO_IMAGE -> word.getImagePath().equals(answer);
            case IMAGE_TO_WORD -> word.getWord().equalsIgnoreCase(answer);
            case AUDIO_TO_WORD -> word.getAudioPath().equals(answer);
        };
    }

    public LearningMaterialDTO generateLearningMaterial(Long levelId, UserDetails userDetails, LearningType type) {
        List<Word> words = wordRepository.findByLevelIdAndActiveForTestingTrue(levelId);

        if (words.isEmpty()) {
            throw new ApiException(
                    BusinessErrorCodes.NO_ACTIVE_WORD,
                    "No active words in level ID: " + levelId
            );
        }

        levelRepository.findById(levelId)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.LEVEL_NOT_FOUND,
                        "Level ID: " + levelId
                ));

        return userRepository.findByUsername(userDetails.getUsername())
                .map(user -> {
                    List<Word> filteredWords = filterWordsByType(words, type);

                    if (filteredWords.size() < 4) {
                        throw new ApiException(
                                BusinessErrorCodes.NOT_ENOUGH_WORDS,
                                "Not enough words with the required attributes in level ID: " + levelId
                        );
                    }

                    Word targetWord = selectWordBasedOnProgress(filteredWords, user.getId());
                    List<String> options = generateOptions(targetWord, filteredWords, type);
                    return new LearningMaterialDTO(
                            wordMapper.toWordDto(targetWord),
                            options,
                            type
                    );
                })
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.USER_NOT_FOUND,
                        "User: " + userDetails.getUsername()
                ));
    }

    private List<Word> filterWordsByType(List<Word> words, LearningType type) {
        return words.stream()
                .filter(word -> switch (type) {
                    case WORD_TO_IMAGE -> word.getImagePath() != null && !word.getImagePath().isEmpty();
                    case IMAGE_TO_WORD -> word.getWord() != null && !word.getWord().isEmpty();
                    case AUDIO_TO_WORD -> word.getAudioPath() != null && !word.getAudioPath().isEmpty();
                    default -> false;
                })
                .collect(Collectors.toList());
    }

    private Word selectWordBasedOnProgress(List<Word> words, Long userId) {
        Map<Word, Integer> weights = new HashMap<>();
        for (Word word : words) {
            int progress = progressRepository.findByUserIdAndWordId(userId, word.getId())
                    .map(Progress::getProgressValue)
                    .orElse(0);
            weights.put(word, 100 - progress);
        }
        return WeightedRandomSelector.select(words, weights);
    }

    private List<String> generateOptions(Word targetWord, List<Word> allWords, LearningType type) {
        List<String> options = new ArrayList<>();
        List<Word> otherWords = allWords.stream()
                .filter(w -> !w.equals(targetWord))
                .collect(Collectors.toList());

        Collections.shuffle(otherWords);

        switch (type) {
            case WORD_TO_IMAGE:
                options.add(targetWord.getImagePath());
                options.addAll(otherWords.stream()
                        .limit(3)
                        .map(Word::getImagePath)
                        .toList());
                break;
            case IMAGE_TO_WORD:
                options.add(targetWord.getWord());
                options.addAll(otherWords.stream()
                        .limit(3)
                        .map(Word::getWord)
                        .toList());
                break;
            case AUDIO_TO_WORD:
                options.add(targetWord.getAudioPath());
                options.addAll(otherWords.stream()
                        .limit(3)
                        .map(Word::getAudioPath)
                        .toList());
                break;
        }

        Collections.shuffle(options);
        return options;
    }

    private static class WeightedRandomSelector {
        public static <T> T select(List<T> items, Map<T, Integer> weights) {
            int totalWeight = weights.values().stream().mapToInt(Integer::intValue).sum();
            int randomValue = new Random().nextInt(totalWeight);

            int cumulative = 0;
            for (T item : items) {
                cumulative += weights.get(item);
                if (randomValue < cumulative) {
                    return item;
                }
            }
            return items.get(items.size() - 1);
        }
    }
}
