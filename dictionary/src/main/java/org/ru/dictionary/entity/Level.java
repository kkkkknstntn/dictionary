package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Word> words = new HashSet<>();
}