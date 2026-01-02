
package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Order;
import com.shreyashkadam.bookstore.model.OrderItem;
import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Checkout → create order
    @GetMapping("/order/checkout")
    public String checkout(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Order order = orderService.placeOrder(user.getId());

        return "redirect:/order/" + order.getId();
    }


    // Order summary page
    @GetMapping("/order/{orderId}")
    public String orderSummary(@PathVariable Long orderId,
                               HttpSession session,
                               Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        List<OrderItem> items = orderService.getOrderItems(orderId);

        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);

        return "order_summary";
    }

    // User order history
    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        List<Order> orders = orderService.getUserOrders(user.getId());

        model.addAttribute("orders", orders);
        return "order_history";
    }
}
