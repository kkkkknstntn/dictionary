package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "levels")
@Data
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String name;

    private int orderNumber;

    @ManyToMany
    @JoinTable(
            name = "level_words",
            joinColumns = @JoinColumn(name = "level_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Set<Word> words = new HashSet<>();
}