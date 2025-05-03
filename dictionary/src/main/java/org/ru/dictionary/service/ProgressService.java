package org.ru.dictionary.service;

import org.ru.dictionary.entity.User;

public interface ProgressService {
    void updateProgress(User user, Long wordId, int delta);
    Integer getProgress(Long userId, Long wordId);
}