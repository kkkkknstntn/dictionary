package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByLevelIdAndActiveForTestingTrue(Long levelId);
    List<Word> findByLevelId(Long levelId);
    Integer countByLevelId(Long levelId);
    Optional<Word> findTopByLevelIdOrderByOrderNumberDesc(Long levelId);
}