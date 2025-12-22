package com.shreyashkadam.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private double price;

    private String category;

    private int stock;

    @Column(length = 1000)
    private String description;

    private String imageUrl; // Optional
}
