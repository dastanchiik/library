package org.example.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String isbn;
    @Column(length = 1000)
    private String description;

    private String imageURL;

    @Column(nullable = false)
    private int stock;

    private int salesCount = 0;

    @Column(nullable = false)
    private double price = 0;



}