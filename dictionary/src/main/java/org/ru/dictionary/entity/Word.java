package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "definition")
    private String definition;

    @Column(name = "audioPath")
    private String audioPath;

    @Column(name = "videoPath")
    private String videoPath;
}
