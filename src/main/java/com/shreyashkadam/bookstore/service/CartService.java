package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.repository.BookRepository;
import com.shreyashkadam.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    // Add a book to the user's cart
    public void addToCart(Long userId, Long bookId) {

        CartItem existingItem = cartRepository.findByUserIdAndBookId(userId, bookId);

        if (existingItem != null) {
            // If already in cart, increase quantity
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepository.save(existingItem);
        } else {
            // Else create new cart item
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setBookId(bookId);
            newItem.setQuantity(1);
            cartRepository.save(newItem);
        }
    }

    // Get all items of a user
    public List<CartItem> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Update item quantity
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            item.setQuantity(quantity);
            cartRepository.save(item);
        }
    }

    // Remove item from cart
    public void removeItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    // Calculate cart total
    public double calculateTotal(Long userId) {
        List<CartItem> cartItems = getUserCart(userId);

        double total = 0;

        for (CartItem item : cartItems) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book != null) {
                total += book.getPrice() * item.getQuantity();
            }
        }

        return total;
    }
}
