package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.model.Order;
import com.shreyashkadam.bookstore.model.OrderItem;
import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.repository.BookRepository;
import com.shreyashkadam.bookstore.repository.CartRepository;
import com.shreyashkadam.bookstore.repository.OrderItemRepository;
import com.shreyashkadam.bookstore.repository.OrderRepository;
import com.shreyashkadam.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;   // ✅ ADDED
    private final EmailService emailService;       // ✅ ADDED

    // Place order (checkout)
    public Order placeOrder(Long userId) {

        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return null;
        }

        double total = 0;

        // Calculate total
        for (CartItem item : cartItems) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book != null) {
                total += book.getPrice() * item.getQuantity();
            }
        }

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());

        order = orderRepository.save(order);

        // Create order items
        for (CartItem item : cartItems) {

            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book == null) continue;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setBookId(book.getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(book.getPrice());

            orderItemRepository.save(orderItem);
        }

        // Clear cart
        cartRepository.deleteAll(cartItems);

        // ✅ FETCH USER EMAIL FROM DATABASE
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ SEND EMAIL RECEIPT
        emailService.sendOrderReceipt(
                user.getEmail(),
                order,
                orderItemRepository.findByOrderId(order.getId())
        );

        return order;
    }

    // Get orders for a user
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Get items in a specific order
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
