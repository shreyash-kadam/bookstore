
package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.repository.BookRepository;
import com.shreyashkadam.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    // Add item to cart
    public void addToCart(Long userId, Long bookId) {

        CartItem existing = cartRepository.findByUserIdAndBookId(userId, bookId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            cartRepository.save(existing);
            return;
        }

        CartItem newItem = new CartItem();
        newItem.setUserId(userId);
        newItem.setBookId(bookId);
        newItem.setQuantity(1);

        cartRepository.save(newItem);
    }

    // Get all cart items for a user
    public List<CartItem> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Update quantity
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

    // Calculate total cart amount
    public double calculateTotal(Long userId) {

        List<CartItem> items = getUserCart(userId);
        double total = 0;

        for (CartItem item : items) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book != null) {
                total += book.getPrice() * item.getQuantity();
            }
        }

        return total;
    }
}
