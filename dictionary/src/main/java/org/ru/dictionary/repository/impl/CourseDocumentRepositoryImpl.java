package org.ru.dictionary.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.dictionary.document.CourseDocument;
import org.ru.dictionary.enums.BusinessErrorCodes;
import org.ru.dictionary.exception.ApiException;
import org.ru.dictionary.repository.CourseDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CourseDocumentRepositoryImpl implements CourseDocumentRepository {

    private final ElasticsearchClient elasticsearchClient;

    public List<CourseDocument> searchCourses(String query) {
        try {
            SearchResponse<CourseDocument> response = elasticsearchClient.search(s -> s
                            .index("courses")
                            .query(q -> q
                                    .bool(b -> b
                                            .should(sh -> sh
                                                    .match(m -> m
                                                            .field("title")
                                                            .query(query)
                                                    )
                                            )
                                            .should(sh -> sh
                                                    .match(m -> m
                                                            .field("description")
                                                            .query(query)
                                                    )
                                            )
                                            .should(sh -> sh
                                                    .nested(n -> n
                                                            .path("levels")
                                                            .query(nq -> nq
                                                                    .match(m -> m
                                                                            .field("levels.name")
                                                                            .query(query)
                                                                    )
                                                            )
                                                    )
                                            )
                                            .should(sh -> sh
                                                    .nested(n -> n
                                                            .path("levels")
                                                            .query(levelsQuery -> levelsQuery
                                                                    .nested(nw -> nw
                                                                            .path("levels.words")
                                                                            .query(wordQuery -> wordQuery
                                                                                    .bool(bq -> bq
                                                                                            .should(wordMatch -> wordMatch
                                                                                                    .match(m -> m
                                                                                                            .field("levels.words.word")
                                                                                                            .query(query)
                                                                                                    )
                                                                                            )
                                                                                            .should(wordMatch -> wordMatch
                                                                                                    .match(m -> m
                                                                                                            .field("levels.words.definition")
                                                                                                            .query(query)
                                                                                                    )
                                                                                            )
                                                                                    )
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            ),
                    CourseDocument.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(BusinessErrorCodes.FILE_UPLOAD_FAILED, e.getMessage());
        }
    }
}

