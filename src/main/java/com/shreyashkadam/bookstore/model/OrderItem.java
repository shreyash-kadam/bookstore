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

    private Long orderId;   // Which order this belongs to

    private Long bookId;    // Which book was purchased

    private int quantity;   // Number of copies

    private double price;   // Price per copy at the time of order
}
