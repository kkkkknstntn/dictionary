package org.ru.dictionary.repository;

import org.ru.dictionary.document.CourseDocument;

import java.util.List;

public interface CourseDocumentRepository {
    List<CourseDocument> searchCourses(String keyword);
}
