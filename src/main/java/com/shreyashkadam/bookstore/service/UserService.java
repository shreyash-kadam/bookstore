package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register user
    public User register(User user) {

        // check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null; // email exists → registration failed
        }

        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Validate login
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null; // email not found
        }

        // check password
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }

        return null; // wrong password
    }
}
