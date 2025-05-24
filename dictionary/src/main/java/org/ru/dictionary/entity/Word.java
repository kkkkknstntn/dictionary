package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cascade;

@Data
@RequiredArgsConstructor
@Entity
@Table
@Builder
@AllArgsConstructor
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "definition")
    private String definition;

    @Column(name = "audioPath")
    private String audioPath;

    @Column(name = "videoPath")
    private String videoPath;

    @Column(name = "imagePath")
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Column(name = "active_for_testing", columnDefinition = "boolean default true")
    private boolean activeForTesting = true;

    private Integer orderNumber;
}
