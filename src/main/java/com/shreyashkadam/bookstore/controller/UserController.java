
package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ===================== REGISTER =====================

    // Show register page (PUBLIC)
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle register form
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               Model model) {

        boolean registered = userService.register(user);

        if (!registered) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        return "redirect:/login";
    }

    // ===================== LOGIN =====================

    // Show login page (PUBLIC)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {

        User user = userService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        // 🔥 Store required session data
        session.setAttribute("loggedUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());

        //  Redirect to books page after login
        return "redirect:/books";
    }

    // ===================== LOGOUT =====================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
