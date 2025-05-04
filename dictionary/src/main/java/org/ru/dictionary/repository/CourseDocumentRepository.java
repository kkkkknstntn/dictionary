package org.ru.dictionary.repository;

import org.ru.dictionary.document.CourseDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CourseDocumentRepository {
    List<CourseDocument> searchCourses(String keyword);
}
