package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Order;
import com.shreyashkadam.bookstore.model.OrderItem;
import com.shreyashkadam.bookstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Checkout → Place Order
    @GetMapping("/order/checkout")
    public String checkout(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Order order = orderService.placeOrder(userId);

        if (order == null) {
            return "redirect:/cart";
        }

        return "redirect:/order/" + order.getId();
    }

    // Order Summary Page
    @GetMapping("/order/{orderId}")
    public String orderSummary(@PathVariable Long orderId, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        List<OrderItem> items = orderService.getOrderItems(orderId);

        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);

        return "order_summary";
    }

    // Order History Page
    @GetMapping("/orders")
    public String orderHistory(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        List<Order> orders = orderService.getUserOrders(userId);

        model.addAttribute("orders", orders);

        return "order_history";
    }
}
