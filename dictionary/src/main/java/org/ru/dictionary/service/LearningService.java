package org.ru.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.ru.dictionary.dto.AnswerResultDTO;
import org.ru.dictionary.dto.AnswerSubmissionDTO;
import org.ru.dictionary.dto.LearningMaterialDTO;
import org.ru.dictionary.dto.word.WordResponseDTO;
import org.ru.dictionary.entity.Level;
import org.ru.dictionary.entity.Word;
import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.enums.LearningType;
import org.ru.dictionary.repository.LevelRepository;
import org.ru.dictionary.repository.ProgressRepository;
import org.ru.dictionary.repository.WordRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningService {

    private final ProgressService progressService;
    private final LevelRepository levelRepository;
    private final WordRepository wordRepository;
    private final ProgressRepository progressRepository;
    private final Random random = new Random();

    @Transactional
    public AnswerResultDTO processAnswer(AnswerSubmissionDTO submission, Long userId) {
        boolean isCorrect = validateAnswer(
                submission.getWordId(),
                submission.getAnswer(),
                submission.getType()
        );

        progressService.updateProgress(
                userId,
                submission.getWordId(),
                isCorrect ? 10 : -5
        );

        int newProgress = progressService.getProgress(userId, submission.getWordId());

        return new AnswerResultDTO(isCorrect, newProgress);
    }

    private boolean validateAnswer(Long wordId, String answer, LearningType type) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ResourceNotFoundException("Word not found"));

        return switch (type) {
            case WORD_TO_IMAGE -> word.getImagePath().equals(answer);
            case IMAGE_TO_WORD -> word.getWord().equalsIgnoreCase(answer);
        };
    }

    public LearningMaterialDTO generateLearningMaterial(Long courseId, Long levelId, Long userId, LearningType type) {
        Level level = levelRepository.findByCourseIdAndId(courseId, levelId)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        List<Word> words = new ArrayList<>(level.getWords());
        if (words.isEmpty()) {
            throw new IllegalArgumentException("Level has no words");
        }

        Word targetWord = selectWordBasedOnProgress(words, userId);
        List<String> options = generateOptions(targetWord, words, type);

        return new LearningMaterialDTO(
                mapToWordDTO(targetWord),
                options,
                type
        );
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

        if (type == LearningType.WORD_TO_IMAGE) {
            options.add(targetWord.getImagePath());
            options.addAll(otherWords.stream()
                    .limit(3)
                    .map(Word::getImagePath)
                    .toList());
        } else {
            options.add(targetWord.getWord());
            options.addAll(otherWords.stream()
                    .limit(3)
                    .map(Word::getWord)
                    .toList());
        }

        Collections.shuffle(options);
        return options;
    }

    private WordResponseDTO mapToWordDTO(Word word) {
        return new WordResponseDTO(
                word.getId(),
                word.getWord(),
                word.getDefinition(),
                word.getImagePath(),
                word.getAudioPath(),
                word.getVideoPath()
        );
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
            return items.get(items.size()-1);
        }
    }
}