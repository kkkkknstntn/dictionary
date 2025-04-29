package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserIdAndWordId(long userId, long wordId);
}
