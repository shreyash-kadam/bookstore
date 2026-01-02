
package com.shreyashkadam.bookstore.service;

import com.shreyashkadam.bookstore.model.User;
import com.shreyashkadam.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register new user
    public boolean register(User user) {

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    // Login user
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null) return null;

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }

        return null;
    }
}
