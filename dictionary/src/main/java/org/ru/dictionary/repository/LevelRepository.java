package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByCourseIdAndId(Long courseId, Long levelId);

}
