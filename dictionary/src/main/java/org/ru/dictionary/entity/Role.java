package org.ru.dictionary.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
