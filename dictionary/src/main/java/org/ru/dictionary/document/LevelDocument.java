package org.ru.dictionary.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelDocument {
    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Nested)
    private List<WordDocument> words;
}