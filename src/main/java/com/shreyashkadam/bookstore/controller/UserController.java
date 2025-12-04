package com.shreyashkadam.bookstore.controller;

import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show register page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle register form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

        User savedUser = userService.register(user);

        if (savedUser == null) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        return "redirect:/login";
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        User user = userService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        session.setAttribute("loggedUser", user);

        return "redirect:/";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
