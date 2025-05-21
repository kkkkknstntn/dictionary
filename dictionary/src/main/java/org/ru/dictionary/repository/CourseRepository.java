package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByParticipantsContaining(User participant);
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.participants WHERE c.id = :id")
    Optional<Course> findByIdWithParticipants(@Param("id") Long id);
    List<Course> findByParticipantsId(Long userId);
}
