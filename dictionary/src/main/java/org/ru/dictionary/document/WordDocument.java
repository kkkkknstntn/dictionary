package org.ru.dictionary.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class WordDocument {
    @Field(type = FieldType.Text)
    private String word;

    @Field(type = FieldType.Text)
    private String definition;
}