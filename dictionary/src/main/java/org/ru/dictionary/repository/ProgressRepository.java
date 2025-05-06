package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Progress;
import org.ru.dictionary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserIdAndWordId(long userId, long wordId);
    @Query("SELECT p FROM Progress p " +
            "WHERE p.user.id = :userId " +
            "AND p.word.level.id = :levelId")
    List<Progress> findByUserIdAndLevelId(@Param("userId") Long userId,
                                          @Param("levelId") Long levelId);
}
