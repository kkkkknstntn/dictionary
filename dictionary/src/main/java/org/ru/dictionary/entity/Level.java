package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Course course;

    @Column(nullable = false)
    private String name;

    private int orderNumber;

    @OneToMany(
            mappedBy = "level",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("orderNumber ASC")
    private Set<Word> words = new HashSet<>();
}