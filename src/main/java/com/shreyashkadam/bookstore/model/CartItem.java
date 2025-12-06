package com.shreyashkadam.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;   // which user added this item

    private Long bookId;   // which book was added

    private int quantity;  // how many copies of the book
}
