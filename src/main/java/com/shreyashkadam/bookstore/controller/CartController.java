package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.service.BookService;
import com.shreyashkadam.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    // Add book to cart
    @GetMapping("/cart/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Must be logged in
        }

        cartService.addToCart(userId, bookId);

        return "redirect:/cart";
    }

    // Display user's cart
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getUserCart(userId);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("total", cartService.calculateTotal(userId));

        return "cart"; // cart.html template
    }

    // Update quantity
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam int quantity) {

        cartService.updateQuantity(cartItemId, quantity);

        return "redirect:/cart";
    }

    // Remove item
    @GetMapping("/cart/delete/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId) {

        cartService.removeItem(cartItemId);

        return "redirect:/cart";
    }
}
