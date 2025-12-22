package com.shreyashkadam.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;   // Links to Order table

    private Long bookId;    // Book purchased

    private int quantity;

    private double price;   // Price per item at purchase time
}
