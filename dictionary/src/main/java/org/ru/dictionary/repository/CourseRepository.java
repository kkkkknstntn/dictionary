package org.ru.dictionary.repository;

import org.ru.dictionary.entity.Course;
import org.ru.dictionary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByParticipantsContaining(User participant);
    List<Course> findByParticipantsId(Long userId);
}
