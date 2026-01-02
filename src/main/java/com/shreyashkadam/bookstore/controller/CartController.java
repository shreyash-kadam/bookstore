
package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.Book;
import com.shreyashkadam.bookstore.model.CartItem;
import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.service.BookService;
import com.shreyashkadam.bookstore.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    // Add item to cart
    @GetMapping("/cart/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        cartService.addToCart(user.getId(), bookId);

        return "redirect:/cart";
    }

    // View cart
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        List<CartItem> items = cartService.getUserCart(user.getId());

        model.addAttribute("cartItems", items);
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("total", cartService.calculateTotal(user.getId()));

        return "cart"; // cart.html
    }

    // Update cart quantity
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam int quantity,
                                 HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        cartService.updateQuantity(cartItemId, quantity);

        return "redirect:/cart";
    }

    // Remove item
    @GetMapping("/cart/delete/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId,
                             HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        cartService.removeItem(cartItemId);

        return "redirect:/cart";
    }
}
