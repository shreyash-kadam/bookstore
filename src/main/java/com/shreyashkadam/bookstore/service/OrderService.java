package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.model.Order;
import com.shreyashkadam.bookstore.model.OrderItem;
import com.shreyashkadam.bookstore.repository.BookRepository;
import com.shreyashkadam.bookstore.repository.CartRepository;
import com.shreyashkadam.bookstore.repository.OrderItemRepository;
import com.shreyashkadam.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    // Place order (checkout)
    public Order placeOrder(Long userId) {

        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return null; // cannot place empty order
        }

        double total = 0;

        // calculate total
        for (CartItem item : cartItems) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book != null) {
                total += book.getPrice() * item.getQuantity();
            }
        }

        // create order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);

        // create order items
        for (CartItem item : cartItems) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);

            if (book != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setBookId(book.getId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(book.getPrice());
                orderItemRepository.save(orderItem);
            }
        }

        // clear cart
        cartRepository.deleteAll(cartItems);

        return order;
    }

    // Order History
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Items inside a specific order
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
